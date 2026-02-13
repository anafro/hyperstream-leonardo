package ru.anafro.hyperstream.leonardo.storage;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import ru.anafro.hyperstream.leonardo.metadata.ProfilePicture;
import ru.anafro.hyperstream.leonardo.utils.images.ImageDecoder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import static ru.anafro.hyperstream.leonardo.utils.sugar.CheckedExceptions.rethrowUnchecked;

public class S3ProfilePictureRepository extends ProfilePictureRepository {
    private final S3Client s3;
    private final String bucket;

    public S3ProfilePictureRepository(String host, String accessKey, String secretKey, String region, String bucket) {
        final var awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        final var awsCredentialsProvider = StaticCredentialsProvider.create(awsCredentials);
        final var awsRegion = Region.of(region);
        final var awsEndpointURI = rethrowUnchecked(() -> new URI("http://%s:4566/".formatted(host)));

        this.bucket = bucket;
        this.s3 = S3Client.builder()
                .endpointOverride(awsEndpointURI)
                .region(awsRegion)
                .credentialsProvider(awsCredentialsProvider)
                .build();
        this.createS3BucketIfAbsent();
    }

    @Override
    public Optional<ProfilePicture> getById(int id) {
        try {
            final var bytes = s3.getObject(request -> request
                    .bucket(bucket)
                    .key(String.valueOf(id))).readAllBytes();
            return ImageDecoder.decodePngBytesToImage(bytes).map(image -> new ProfilePicture(id, image));
        } catch (final IOException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(int id) {
        s3.deleteObject(request -> request
                .bucket(bucket)
                .key(String.valueOf(id)));
    }

    @Override
    public void store(ProfilePicture profilePicture) {
        s3.putObject(request -> request
                .bucket(bucket)
                .key(String.valueOf(profilePicture.id())),
                RequestBody.fromBytes(profilePicture.bytes()));
    }

    private void createS3BucketIfAbsent() {
        try {
            s3.headBucket(request -> request.bucket(bucket));
        } catch (final NoSuchBucketException exception) {
            s3.createBucket(request -> request.bucket(bucket));
        }
    }
}
