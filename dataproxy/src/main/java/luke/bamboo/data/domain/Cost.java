package luke.bamboo.data.domain;

public class Cost {
	private int gold;
	private int wood;
	private int stone;
	private int iron;
	private int diamonds;
	private int crystal;
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
	public void percent(int percent) {
		gold = (int)((float)gold/100*percent);
		wood = (int)((float)wood/100*percent);
		stone = (int)((float)stone/100*percent);
		iron = (int)((float)iron/100*percent);
		diamonds = (int)((float)diamonds/100*percent);
		crystal = (int)((float)crystal/100*percent);
	}
	public void invert() {
		gold = -gold;
		wood = -wood;
		stone = -stone;
		iron = -iron;
		diamonds = -diamonds;
		crystal = -crystal;
	}
}
