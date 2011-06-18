package net.wirog.bukkit.cozybed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * 簡易設定ファイルマネージャ
 * 
 * @author wiro
 */
public class PropertyManager {
	private Properties prop;
	private File file;
	private Map<String, String> comments = new HashMap<String, String>();
	
	/**
	 * 新しくプロパティマネージャを生成します。
	 * 
	 * @param plugin
	 */
	public PropertyManager(JavaPlugin plugin) {
		this.prop = new Properties();
		this.file = new File(plugin.getDataFolder(), plugin.getDescription().getName() + ".properties");
		
		// データディレクトリの作成
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		
		// ファイルの読み込み
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			prop.load(fis);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * プロパティをファイルに保存します。
	 * 
	 * @param plugin
	 */
	public void save() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(file);
			
			// コメントと共に書き出す
			for (Object obj : prop.keySet()) {
				String key = obj.toString();
				
				String comment = comments.get(key);
				if (comment != null)
					pw.append('#').append(' ').println(comment);
				pw.append(key).append('=').append(prop.getProperty(key)).println();
				pw.println();
			}
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pw != null)
				pw.close();
		}
	}
	
	/**
	 * 設定ファイルが存在するかどうか
	 * 
	 * @return
	 */
	public boolean isFileExists() {
		return file.exists();
	}
	
	public String get(String key, Object def) {
		String ret = prop.getProperty(key);
		if (ret == null) {
			// とりあえずデフォルト値を設定しておく
			prop.setProperty(key, def.toString());
			return def.toString();
		}
		return ret;
	}

	public boolean getBoolean(String key, boolean def) {
		try {
			return Boolean.getBoolean(get(key, def));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return def;
	}
	
	public int getInt(String key, int def) {
		try {
			return Integer.parseInt(get(key, def));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return def;
	}
	
	public long getLong(String key, long def) {
		try {
			return Long.parseLong(get(key, def));
		} catch (Exception e) {
			e.printStackTrace();
			}
		return def;
	}

	public float getFloat(String key, float def) {
		try {
			return Float.parseFloat(get(key, def));
		} catch (Exception e) {
			e.printStackTrace();
			}
		return def;
	}
	
	public double getDouble(String key, double def) {
		try {
			return Double.parseDouble(get(key, def));
		} catch (Exception e) {
			e.printStackTrace();
			}
		return def;
	}

	public Object set(String key, Object val) {
		return prop.setProperty(key, val.toString());
	}
	
	public void setComment(String key, String comment) {
		comments.put(key, comment);
	}
}
