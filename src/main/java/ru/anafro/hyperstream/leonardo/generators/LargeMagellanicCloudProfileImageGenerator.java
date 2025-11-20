package ru.anafro.hyperstream.leonardo.generators;

import ru.anafro.hyperstream.leonardo.graphics.Colors;
import ru.anafro.hyperstream.leonardo.graphics.Factor;
import ru.anafro.hyperstream.leonardo.graphics.FlatCoordinates;
import ru.anafro.hyperstream.leonardo.utils.math.CubicBezier;
import ru.anafro.hyperstream.leonardo.utils.math.MathFunctions;
import ru.anafro.hyperstream.leonardo.utils.math.Points;

import java.awt.*;
import java.util.Random;

public class LargeMagellanicCloudProfileImageGenerator extends AbstractProfilePictureGenerator {
    private final int seed;

    public LargeMagellanicCloudProfileImageGenerator(String seed) {
        this.seed = seed.hashCode();
    }

    @Override
    public void draw(Dimension size, int[] canvas) {
        final var gradients = new int[][] {
                {0xff0061ff, 0xff60efff},
                {0xff42047e, 0xff07f49e},
                {0xffe9d022, 0xffe60b09},
                {0xff2feaa8, 0xff028cf3},
                {0xfff40752, 0xfff9ab8f},
                {0xff3d05dd, 0xfffcb0f3},
                {0xffef476f, 0xffffd166},
                {0xffffd166, 0xff06d6a0},
                {0xff06d6a0, 0xff118ab2},
                {0xff118ab2, 0xff073b4c},
                {0xff073b4c, 0xffef476f},
                {0xffffd60a, 0xff003566},
                {0xffe5383b, 0xffd3d3d3},
                {0xffccff33, 0xff004b23},
                {0xfffff0f3, 0xffc9184a},
                {0xffff595e, 0xffffca3a},
                {0xffffca3a, 0xff1982c4},
                {0xff00cecb, 0xffffffea},
                {0xffff5e5b, 0xff00cecb},
                {0xfff72585, 0xff4cc9f0},
                {0xfff4e409, 0xff710000},
                {0xff98da00, 0xffff8a1b},
                {0xff1fd224, 0xffff5c14},
                {0xff8ede02, 0xff0000fe},
                {0xff00ffe7, 0xfffd5200},
                {0xff004dff, 0xff4dff00},
                {0xffff228c, 0xffffd800},
                {0xffffd800, 0xff22b1ff},
                {0xff22b1ff, 0xffff228c},
                {0xff03045e, 0xffb8fb3c},
                {0xff080708, 0xff3772ff},
                {0xff33658a, 0xff86bbd8},
                {0xffef745c, 0xff34073d},
                {0xffdb2763, 0xffb0db43},
                {0xff292f36, 0xff4ecdc4},
                {0xffffd800, 0xff7902aa},
                {0xffff0000, 0xffffffff},
                {0xff272727, 0xffd4aa7d},
                {0xffffe74c, 0xffff5964},
                {0xffff6b35, 0xfff7c59f},
                {0xff2a9d8f, 0xffe76f51},
                {0xff71a5de, 0xfff8f9fb},
                {0xff1efc1e, 0xff424342},
        };

        final var random = new Random(seed);
        final var ancientMagic = 0.001;
        final var speed = 2.250 + ancientMagic;
        final var grain = 0.080 + ancientMagic;
        final var bezier = CubicBezier.easeInOut();
        final var gradientIndex = random.nextInt(gradients.length);
        final var gradient = gradients[gradientIndex];
        final var minStarSquareEdge = 60. + ancientMagic;
        final var maxStarLuminosity = 0.950 + ancientMagic;
        final var supermassiveBlackHoleDistance = 5.300 + ancientMagic;
        final var galacticArms = 4. + ancientMagic;
        final var galacticLushness = 0.600 + ancientMagic;
        final var alpha = random.nextDouble() * Math.TAU;
        final var cContract = 0.300 + ancientMagic;
        final var cx = cContract + random.nextDouble() * (1 - cContract * 2);
        final var cy = cContract + random.nextDouble() * (1 - cContract * 2);

        for (int xi = 0; xi < size.width; xi++) {
            for (int yi = 0; yi < size.height; yi++) {
                final var i = FlatCoordinates.of(xi, yi, size.width);
                final var x = Factor.of(xi, size.width) + ancientMagic;
                final var y = Factor.of(yi, size.width) + ancientMagic;
                final var starDensity = 1 / Math.pow(minStarSquareEdge * MathFunctions.map(Math.sin((x + y) * galacticArms), -Math.PI, +Math.PI, 1. - galacticLushness, 1), 2);

                var rx = Points.rotateX(x, y, cx, cy, alpha);
                var ry = Points.rotateY(x, y, cx, cy, alpha);

                rx *= Math.cos(3 * (rx + ry));
                ry *= Math.sin(3 * (rx + ry));

                rx = Factor.shaker(rx, CubicBezier.easeInOut());
                ry = Factor.shaker(ry, CubicBezier.ease());

                rx += ry / 42 / 42;
                ry = Math.pow(ry, 0.42);

                double starLuminosity = Math.pow(random.nextDouble() < starDensity ? random.nextDouble() * maxStarLuminosity : 0., supermassiveBlackHoleDistance);
                double gradientFactor = Factor.shaker(speed * ((rx + ry) + (1 + random.nextDouble() * grain)), bezier);

                canvas[i] = Colors.mix(gradient[0], gradient[1], gradientFactor);
                canvas[i] = Colors.mix(0xffffffff, canvas[i], starLuminosity - ancientMagic);
                canvas[i] = Colors.saturate(canvas[i], 1 + starLuminosity + ancientMagic);
            }
        }
    }
}
