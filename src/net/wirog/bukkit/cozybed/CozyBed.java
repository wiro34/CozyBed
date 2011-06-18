package net.wirog.bukkit.cozybed;

import java.util.logging.Logger;

import net.wirog.bukkit.cozybed.event.PlayerListener;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 一定数以上のプレイヤーがベッドに入ったら、全員寝ていなくても朝にするプラグイン。
 * 
 * @author wiro
 */
public class CozyBed extends JavaPlugin  {
	
	/** 朝にするために必要な、ベッドで寝ている人の割合 */
	public static double RATIO_OF_SLEEPING;
	
	/** 朝になった際に、ベッドで寝ていた人を回復するかどうか */
	public static boolean HEALING_ON_MORNING;
	
	/** 誰かがベッドに入った際のメッセージ */
	public static String MSG_WENT_TO_BED;
	
	/** 朝起きたときのメッセージ */
	public static String MSG_GOOD_MORNING;
	
	/**
	 * onEnable
	 */
	@Override
	public void onEnable() {
		// プロパティファイルを読み込む
		PropertyManager pm	= new PropertyManager(this);
		RATIO_OF_SLEEPING	= pm.getDouble("ratioOfSleeping", 0.5);
		HEALING_ON_MORNING	= pm.getBoolean("healingOnMorning", true);
		MSG_WENT_TO_BED		= pm.get("message.wentToBed", "[Notice] %s went to bed.");
		MSG_GOOD_MORNING	= pm.get("message.goodMorning", "Good morning.");
		if (!pm.isFileExists()) {
			// コメントを設定して書きだす
			pm.setComment("ratioOfSleeping", "朝にするために必要な、ベッドで寝ている人の割合(0.0～1.0)");
			pm.setComment("healingOnMorning", "朝になった際に、ベッドで寝ていた人を回復するかどうか");
			pm.setComment("message.wentToBed", "誰かがベッドに入った際のメッセージ(最初の %s に寝た人の名前が入ります。)");
			pm.setComment("message.goodMorning", "朝起きたときのメッセージ");
			pm.save();
		}
		
		// イベントリスナを登録
		PlayerListener playerListener = new PlayerListener();
		getServer().getPluginManager().registerEvent(Type.PLAYER_BED_ENTER,		playerListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Type.PLAYER_BED_LEAVE,		playerListener, Priority.Normal, this);

		// ログ表示
		Logger logger = Logger.getLogger(getDescription().getName());
		logger.info(getDescription().getName() + " version " + getDescription().getVersion() + " is enabled.");
	}
	
	/**
	 * onDisable
	 */
	@Override
	public void onDisable() {
	}
}
