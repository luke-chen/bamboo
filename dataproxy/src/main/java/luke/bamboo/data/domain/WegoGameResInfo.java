package luke.bamboo.data.domain;

import java.util.List;


public class WegoGameResInfo {
	Integer baseid;
	String name;
	Integer level;
	Integer maxLv;
	int costGold;
	int costIron;
	int costWood;
	int costStone;
	int updateCoin;
	int updateIron;
	int updateWood;
	int updateStone;
	int bornTimeUp;
	int attack;
	int hp;
	int targetType; //目标
	int atkType; //攻击偏好
	float atkAdd;//偏好加成
	String model; //模型编号
	String iconPath; //icon编号
	float attackInterval; //攻击间隔
	float randomAttackIntervak; //攻击间隔随机值
	int armyType;//兵种类型
	int spaceType;//空地类型
	int space;//所需空间
	List<Integer> skillIds; //技能id[string]
	List<Integer> baseAttributes; //基本属性[int]
	List<Integer> otherAttributes; //其他属性[int]
	float bloodScale;//血条长度

	
	public Cost buildCost() {
		Cost cost = new Cost();
		cost.setGold(costGold);
		cost.setWood(costWood);
		cost.setIron(costIron);
		cost.setStone(costStone);
		return cost;
	}
	
	public Cost buildCost(int multiple) {
		Cost cost = new Cost();
		cost.setGold(costGold*multiple);
		cost.setWood(costWood*multiple);
		cost.setIron(costIron*multiple);
		cost.setStone(costStone*multiple);
		return cost;
	}
	
	public List<Integer> getBaseAttributes() {
		return baseAttributes;
	}

	public void setBaseAttributes(List<Integer> baseAttributes) {
		this.baseAttributes = baseAttributes;
	}

	public List<Integer> getOtherAttributes() {
		return otherAttributes;
	}

	public void setOtherAttributes(List<Integer> otherAttributes) {
		this.otherAttributes = otherAttributes;
	}

	public float getBloodScale() {
		return bloodScale;
	}

	public void setBloodScale(float bloodScale) {
		this.bloodScale = bloodScale;
	}

	public int getSpace() {
		return space;
	}

	public void setSpace(int space) {
		this.space = space;
	}

	public List<Integer> getSkillIds() {
		return skillIds;
	}

	public void setSkillIds(List<Integer> skillIds) {
		this.skillIds = skillIds;
	}

	public float getAttackInterval() {
		return attackInterval;
	}

	public void setAttackInterval(float attackInterval) {
		this.attackInterval = attackInterval;
	}

	public float getRandomAttackIntervak() {
		return randomAttackIntervak;
	}

	public void setRandomAttackIntervak(float randomAttackIntervak) {
		this.randomAttackIntervak = randomAttackIntervak;
	}

	public int getArmyType() {
		return armyType;
	}

	public void setArmyType(int armyType) {
		this.armyType = armyType;
	}

	public int getSpaceType() {
		return spaceType;
	}

	public void setSpaceType(int spaceType) {
		this.spaceType = spaceType;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public int getAtkType() {
		return atkType;
	}

	public void setAtkType(int atkType) {
		this.atkType = atkType;
	}

	public float getAtkAdd() {
		return atkAdd;
	}

	public void setAtkAdd(float atkAdd) {
		this.atkAdd = atkAdd;
	}

	public Integer getBaseid() {
		return baseid;
	}

	public void setBaseid(Integer baseid) {
		this.baseid = baseid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getMaxLv() {
		return maxLv;
	}

	public void setMaxLv(Integer maxLv) {
		this.maxLv = maxLv;
	}

	public int getCostGold() {
		return costGold;
	}

	public void setCostGold(int costGold) {
		this.costGold = costGold;
	}

	public int getCostIron() {
		return costIron;
	}

	public void setCostIron(int costIron) {
		this.costIron = costIron;
	}

	public int getCostWood() {
		return costWood;
	}

	public void setCostWood(int costWood) {
		this.costWood = costWood;
	}

	public int getCostStone() {
		return costStone;
	}

	public void setCostStone(int costStone) {
		this.costStone = costStone;
	}

	public int getUpdateCoin() {
		return updateCoin;
	}

	public void setUpdateCoin(int updateCoin) {
		this.updateCoin = updateCoin;
	}

	public int getUpdateIron() {
		return updateIron;
	}

	public void setUpdateIron(int updateIron) {
		this.updateIron = updateIron;
	}

	public int getUpdateWood() {
		return updateWood;
	}

	public void setUpdateWood(int updateWood) {
		this.updateWood = updateWood;
	}

	public int getUpdateStone() {
		return updateStone;
	}

	public void setUpdateStone(int updateStone) {
		this.updateStone = updateStone;
	}

	public int getBornTimeUp() {
		return bornTimeUp;
	}

	public void setBornTimeUp(int bornTimeUp) {
		this.bornTimeUp = bornTimeUp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	public static boolean isBuildingBaseid(int baseid) {
		return baseid >= 200000 && baseid < 300000;
	}
	
	public static boolean isSoldierBaseid(int baseid) {
		return baseid >= 100101 && baseid < 200000;
	}
	
	public static boolean isHeroBaseid(int baseid) {
		return baseid >= 100000 && baseid < 100101;
	}
	
	public static boolean isSkillBaseid(int baseid) {
		return baseid >= 4100101 && baseid < 4200000;
	}
}
