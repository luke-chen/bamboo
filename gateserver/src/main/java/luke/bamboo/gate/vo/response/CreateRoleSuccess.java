package luke.bamboo.gate.vo.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luke.bamboo.data.domain.Player;

public class CreateRoleSuccess {
	private Player player;
	
	public CreateRoleSuccess() {
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
