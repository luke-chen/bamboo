package luke.bamboo.data.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家对象
 * 
 * @author Luke
 */
public class Player {
	private Long id;
	private Long accountId;
	private String name;
	private int phyle;
	private int vip;
	private int level;
	private int gold;
	private int wood;
	private int stone;
	private int iron;
	private int diamonds;
	private int crystal;
	private int score;
	private int consecutiveWin;
	
	// 主城等级
	private int cityHallLevel;
	/*Map<basic base id, number> 拥有建筑数量*/
	private Map<Integer, Integer> buildingNum = new HashMap<Integer, Integer>();
	/*Map<basic base id, baseid> 拥有建筑的最高等级*/
	private Map<Integer, Integer> buildingLevel = new HashMap<Integer, Integer>();
	
	public int myGetCityHallLevel() {
		return cityHallLevel;
	}

	public void mySetCityHallLevel(int cityHallLevel) {
		this.cityHallLevel = cityHallLevel;
	}

	public Map<Integer, Integer> myGetBuildingNum() {
		return buildingNum;
	}

	public void mySetPlayerBuildingNum(Map<Integer, Integer> buildingNum) {
		this.buildingNum = buildingNum;
	}

	public Map<Integer, Integer> myGetBuildingLevel() {
		return buildingLevel;
	}

	public void mySetBuildingLevel(Map<Integer, Integer> buildingLevel) {
		this.buildingLevel = buildingLevel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPhyle() {
		return phyle;
	}

	public void setPhyle(int phyle) {
		this.phyle = phyle;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getStone() {
		return stone;
	}

	public void setStone(int stone) {
		this.stone = stone;
	}

	public int getIron() {
		return iron;
	}

	public void setIron(int iron) {
		this.iron = iron;
	}

	public int getDiamonds() {
		return diamonds;
	}

	public void setDiamonds(int diamonds) {
		this.diamonds = diamonds;
	}

	public int getCrystal() {
		return crystal;
	}

	public void setCrystal(int crystal) {
		this.crystal = crystal;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getConsecutiveWin() {
		return consecutiveWin;
	}

	public void setConsecutiveWin(int consecutiveWin) {
		this.consecutiveWin = consecutiveWin;
	}
	
	public int getBaseid() {
		return 100000 + level;
	}

	/**
	 * 玩家资源消耗和增加
	 * @param payGold
	 * @param payWood
	 * @param payStone
	 * @param payIron
	 * @param payDiamonds
	 * @param payCrystal
	 * @return
	 */
	public boolean pay(int payGold, int payWood, int payStone, int payIron,
			int payDiamonds, int payCrystal) {
		if (gold - payCrystal >= 0 && wood - payWood >= 0
				&& stone - payStone >= 0 && iron - payIron >= 0
				&& diamonds - payDiamonds >= 0 && crystal - payCrystal >= 0) {
			gold -= payGold;
			wood -= payWood;
			stone -= payStone;
			iron -= payIron;
			diamonds -= payDiamonds;
			crystal -= payCrystal;
			return true;
		}
		return false;
	}
	
	public boolean pay(Cost cost) {
		return pay(cost.getGold(), cost.getWood(), cost.getStone(), cost.getIron(), cost.getDiamonds(), cost.getCrystal());
	}
	
	public boolean unpay(Cost cost) {
		return pay(-cost.getGold(), -cost.getWood(), -cost.getStone(), -cost.getIron(), -cost.getDiamonds(), -cost.getCrystal());
	}
}
