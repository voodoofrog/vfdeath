package proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initSounds()
	{
		// load sounds
	}

	@Override
	public void initRenderers()
	{
		// load renderers
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}
}
