package me.rsk.hiroshima.setting;

import java.util.ArrayList;

import me.rsk.hiroshima.module.Module;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class OSSettingsManager {
	
	private ArrayList<OSSetting> settings;
	
	public OSSettingsManager(){
		this.settings = new ArrayList<>();
	}
	
	public void rSetting(OSSetting in){
		this.settings.add(in);
	}
	
	public ArrayList<OSSetting> getSettings(){
		return this.settings;
	}
	
	public ArrayList<OSSetting> getSettingsByMod(Module mod){
		ArrayList<OSSetting> out = new ArrayList<>();
		for(OSSetting s : getSettings()){
			if(s.getParentMod().equals(mod)){
				out.add(s);
			}
		}
		if(out.isEmpty()){
			return null;
		}
		return out;
	}
	
	public OSSetting getSettingByDisplayName(String name){
		for(OSSetting set : getSettings()){
			if(set.getDisplayName().equalsIgnoreCase(name)){
				return set;
			}
		}
		System.err.println("[Osiris] Error Setting NOT found: '" + name +"'!");
		return null;
	}

	public OSSetting getSettingByID(String id){
		for(OSSetting s : getSettings()){
			if(s.getId().equalsIgnoreCase(id)){
				return s;
			}
		}
		System.err.println("[Osiris] Error Setting NOT found: '" + id +"'!");
		return null;
	}

}