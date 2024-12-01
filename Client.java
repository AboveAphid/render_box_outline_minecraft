package github.aboveaphid.taskswithbots;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.math.BlockPos;

public class TasksWithBotsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		DrawBox.addBlock(new BlockPos(0, 80, 0), Colors.GREEN);
		DrawBox.addBlock(new BlockPos(0, 81, 0), Colors.RED);
		DrawBox.addBlock(new BlockPos(0, 82, 0), Colors.BLUE);

		WorldRenderEvents.LAST.register(context -> {
			final MatrixStack matrixStack = context.matrixStack();
			final MinecraftClient client = MinecraftClient.getInstance();
			DrawBox.initialize(matrixStack, client);
		});
  }
}
