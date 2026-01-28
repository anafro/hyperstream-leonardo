package ru.anafro.hyperstream.leonardo.http;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import ru.anafro.hyperstream.leonardo.metadata.ProfilePicture;
import ru.anafro.hyperstream.leonardo.metadata.ProfilePictureIdConverter;
import ru.anafro.hyperstream.leonardo.storage.ProfilePictureRepository;
import ru.anafro.hyperstream.leonardo.utils.buffering.Bytes;
import ru.anafro.hyperstream.leonardo.utils.images.ImageEncoder;

public class ProfilePictureServer {
    private final Javalin javalin;
    private final ProfilePictureIdConverter idConverter;
    private final ProfilePictureRepository repository;
    private final int port;

    public ProfilePictureServer(
            ProfilePictureIdConverter idConverter,
            ProfilePictureRepository repository,
            final int port) {
        this.idConverter = idConverter;
        this.repository = repository;
        this.port = port;
        this.javalin = Javalin.create();
        this.defineRoutes();
    }

    public void start() {
        this.javalin.start(this.port);
    }

    private void getProfilePicture(Context ctx) {
        final String username = ctx.pathParam("username");
        if (username == null) {
            throw new BadRequestResponse("Username is not provided in URI.");
        }

        final int profilePictureId = this.idConverter.convertUsernameToProfilePictureId(username);
        final ProfilePicture profilePicture = this.repository.getById(profilePictureId)
                .orElseThrow(() -> new NotFoundResponse("Profile picture not found."));
        final Bytes profilePictureBytes = ImageEncoder.encodeImageToPngBytes(profilePicture.image())
                .orElseThrow(() -> new InternalServerErrorResponse("Can't encode an image"));

        ctx.contentType(ContentType.IMAGE_PNG);
        ctx.result(profilePictureBytes.array());
    }

    private void getUp(Context ctx) {
        ctx.contentType(ContentType.TEXT_PLAIN);
        ctx.result("OK");
    }

    private void defineRoutes() {
        this.javalin.get("/@{username}/profile-picture", this::getProfilePicture);
        this.javalin.get("/up", this::getUp);
    }
}
