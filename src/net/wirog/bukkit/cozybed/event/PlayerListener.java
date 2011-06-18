package net.wirog.bukkit.cozybed.event;

import java.util.Set;
import java.util.TreeSet;

import net.wirog.bukkit.cozybed.CozyBed;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;


/**
 * プレイヤーリスナ
 * 
 * @author wiro
 */
public class PlayerListener extends org.bukkit.event.player.PlayerListener {
	// 寝てる人リスト
	private Set<String> sleepers = new TreeSet<String>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		CraftWorld world = (CraftWorld)player.getWorld();
		CraftServer server = world.getHandle().getServer();
		
		// リストに追加
		if (!sleepers.contains(name))
			sleepers.add(name);
		
		StringBuilder sb = new StringBuilder();
		sb.append(ChatColor.AQUA + String.format(CozyBed.MSG_WENT_TO_BED, name));

		// 寝ている人数が一定数を越えているかチェック
		int sleepersCount = sleepers.size();
		int players   = world.getPlayers().size();
		if (sleepersCount >= players * CozyBed.RATIO_OF_SLEEPING) {
			// 寝ている人以外を無視するように設定
			for (Player p : world.getPlayers()) {
				if (sleepers.contains(p.getName()))
					p.setSleepingIgnored(false);
				else
					p.setSleepingIgnored(true);
			}
			sb.append(ChatColor.GREEN);
		} else {
			sb.append(ChatColor.RED);
		}
		
		sb.append('(').append(sleepersCount).append('/').append(players).append(')');
		server.broadcastMessage(sb.toString());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		String name = player.getName();

		// 寝てる人リストから削除
		if (sleepers.contains(name))
			sleepers.remove(player.getName());

		// 寝ている人数が一定数を越えているかチェック
		int sleepersCount = sleepers.size();
		int players   = world.getPlayers().size();
		if (sleepersCount >= players * CozyBed.RATIO_OF_SLEEPING)
			player.setSleepingIgnored(true);
		else
			player.setSleepingIgnored(false);
		
		// 朝起きたらHP満タンにしてあげる
		if (world.getTime() < 500) {
			player.setHealth(20);
			if (CozyBed.MSG_GOOD_MORNING.length() > 0)
				player.sendMessage(ChatColor.LIGHT_PURPLE + CozyBed.MSG_GOOD_MORNING);

			// リセット
			sleepers.clear();
			for (Player p : world.getPlayers())
				p.setSleepingIgnored(false);
		}
	}
}
