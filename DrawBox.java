// Reference: https://discord.com/channels/507304429255393322/807617488313516032/1302046276090859664
// The Fabric Project - baktus_79 - 11/02/2024

package github.aboveaphid.taskswithbots;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import java.util.HashMap;
import java.util.Map;

public class DrawBox {

    private static final HashMap<BlockPos, Integer> blocks = new HashMap<>();

    public static void initialize(MatrixStack matrixStack, MinecraftClient client) {
        if (client.world == null || client.player == null || blocks.isEmpty()) return;

        final Camera camera = client.gameRenderer.getCamera();
        final Vec3d cameraPos = camera.getPos();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR); // (DrawMod.LINE does not work)

        matrixStack.push();

        final Matrix4f matrix = matrixStack.peek().getPositionMatrix();


        // OLD FOR VERSIONS LESS THAN 1.21.2: RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

        for (Map.Entry<BlockPos, Integer> s : blocks.entrySet()) {
            final BlockPos blockPos = s.getKey();
            final int color = s.getValue();
            final double x = blockPos.getX() - cameraPos.x;
            final double y = blockPos.getY() - cameraPos.y;
            final double z = blockPos.getZ() - cameraPos.z;
            final Box bb = new Box(x, y, z, x + 1, y + 1, z + 1);
            drawOutlineBox(matrix, bufferBuilder, bb, color);
        }

        bufferBuilder.color(Colors.RED);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        matrixStack.pop();
    }

    private static void drawOutlineBox(Matrix4f matrix, BufferBuilder bufferBuilder, Box bb, int color) {
        final Vector3f normal = new Vector3f(0, 1, 0);

        drawLine(bufferBuilder, matrix, bb.minX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.minZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.maxX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.maxZ, color, normal);

        drawLine(bufferBuilder, matrix, bb.maxX, bb.minY, bb.maxZ, bb.minX, bb.minY, bb.maxZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.minX, bb.minY, bb.maxZ, bb.minX, bb.minY, bb.minZ, color, normal);

        drawLine(bufferBuilder, matrix, bb.minX, bb.maxY, bb.minZ, bb.maxX, bb.maxY, bb.minZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.maxX, bb.maxY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color, normal);

        drawLine(bufferBuilder, matrix, bb.maxX, bb.maxY, bb.maxZ, bb.minX, bb.maxY, bb.maxZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.minX, bb.maxY, bb.maxZ, bb.minX, bb.maxY, bb.minZ, color, normal);

        drawLine(bufferBuilder, matrix, bb.minX, bb.minY, bb.minZ, bb.minX, bb.maxY, bb.minZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.maxX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.minZ, color, normal);

        drawLine(bufferBuilder, matrix, bb.maxX, bb.minY, bb.maxZ, bb.maxX, bb.maxY, bb.maxZ, color, normal);
        drawLine(bufferBuilder, matrix, bb.minX, bb.minY, bb.maxZ, bb.minX, bb.maxY, bb.maxZ, color, normal);
    }

    private static void drawLine(BufferBuilder bufferBuilder, Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, int color, Vector3f normal) {
        bufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z1).color(color);
        bufferBuilder.vertex(matrix, (float) x2, (float) y2, (float) z2).color(color);
    }

    public static void addBlock(BlockPos blockPos, int color) {
        blocks.put(blockPos, color);
    }

    public static void removeBlock(BlockPos blockPos) {
        blocks.remove(blockPos);
    }
}
