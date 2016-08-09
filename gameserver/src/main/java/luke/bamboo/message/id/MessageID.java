package luke.bamboo.message.id;

public class MessageID {
	public static final short RSP_UNKNOWN = 1;
	public static final short RSP_TOKEN_INVALID = 0002;
	
	// heart beat
	public static final short REQ_HEART_BEAT = 80;
	public static final short RSP_HEART_BEAT = REQ_HEART_BEAT;
	
	// login
	public static final short REQ_LOGIN = 1000;
	public static final short RSP_LOGIN_OK = 1001;
	public static final short RSP_LOGIN_ERROR = 1002;
	
	// fight
	public static final short REQ_SEARCH_OPPONENT = 2001;
	public static final short RSP_SEARCH_OPPONENT_OK = 2002;
	public static final short RSP_SEARCH_OPPONENT_ERROR = 2003;
	
	public static final short REQ_BATTLE_ENTER = 2101;
	public static final short RSP_BATTLE_ENTER_OK = 2102;
	public static final short RSP_BATTLE_ENTER_ERROR = 2103;
	
	public static final short REQ_BATTLE_ENTER_TEST = 2104;
	
	public static final short REQ_BATTLE_ATTACK = 2202;
	public static final short RSP_BATTLE_ATTACT_OK = 2203;
	public static final short RSP_BATTLE_ATTACT_ERROR_HP = 2204;
	public static final short RSP_BATTLE_FOUND_ERROR = 2205;
	
	public static final short REQ_BATTLE_DISPATCH_INVASION = 2500;
	public static final short RSP_BATTLE_DISPATCH_INVASION_OK = 2501;
	public static final short RSP_BATTLE_DISPATCH_INVASION_ERROR = 2502;
	
	public static final short REQ_BATTLE_DISPATCH_DEFENCE = 2600;
	public static final short RSP_BATTLE_DISPATCH_DEFENCE_OK = 2601;
	public static final short RSP_BATTLE_DISPATCH_DEFENCE_ERROR = 2602;
	
	public static final short REQ_BATTLE_OVER = 2302;
	public static final short RSQ_BATTLE_OVER_OK = 2303;
	public static final short RSQ_BATTLE_OVER_ERROR = 2304;
	
	// build
	public static final short REQ_BUILD =  3000;
	public static final short RSP_BUILD_OK =  3001;
	public static final short RSP_BUILD_ERROR =  3002;
	
	public static final short REQ_UP_BUILD =  3100;
	public static final short RSP_UP_BUILD_OK =  3101;
	public static final short RSP_UP_BUILD_ERROR =  3102;
	
	public static final short REQ_CANCEL_BUILD =  3200;
	public static final short RSP_CANCEL_BUILD_OK =  3201;
	public static final short RSP_CANCEL_BUILD_ERROR =  3202;
	
	public static final short REQ_REFRSH_INFO =  3103;
	public static final short RSP_REFRSH_INFO =  3104;
	
	public static final short REQ_MOVE_BUILD =  3105;
	public static final short RSP_MOVE_BUILD_OK =  3106;
	public static final short RSP_MOVE_BUILD_ERROR =  3107;
	
	public static final short REQ_MAKE_GOODS =  3108;
	public static final short RSP_MAKE_GOODS_OK =  3109;
	public static final short RSP_MAKE_GOODS_ERROR =  3110;
	
	public static final short REQ_UP_GOODS =  3120;
	public static final short RSP_UPDATE_GOODS_OK =  3121;
	public static final short RSP_UPDATE_GOODS_ERROR =  3122;
	
	public static final short REQ_CANCEL_UP_SOLDIER =  3130;
	public static final short RSP_CANCEL_UP_SOLDIER_OK =  3131;
	public static final short RSP_CANCEL_UP_SOLDIER_ERROR =  3132;
	
	public static final short REQ_UP_PLAYER =  3140;
	public static final short RSP_UP_PLAYER_OK =  3141;
	public static final short RSP_UP_PLAYER_ERROR =  3142;
	
	public static final short REQ_BUY_FINISH = 3201;
	public static final short REQ_BUY_FINISH_OK = 3202;
	public static final short REQ_BUY_FINISH_ERROR = 3203;
	
	public static final short REQ_PICK =  3400;
	public static final short RSP_PICK_OK =  3401;
	public static final short RSP_PICK_ERROR =  3402;
}
