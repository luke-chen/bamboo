package luke.bamboo.monitor;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;

import luke.bamboo.netty.GameContext;
import luke.bamboo.netty.JsonUtil;

public class Reporter extends TimerTask {
	CuratorFramework client;
	String host;

	public Reporter(String host, String zkStr) {
		this.host = host;
		client = CuratorFrameworkFactory.newClient(zkStr, new ExponentialBackoffRetry(1000, 3));
		client.start();
	}

	@Override
	public void run() {
		try {
			GameStatus status = new GameStatus();
			status.setActiveNum(GameContext.activeNum.get());
			status.setTime(Calendar.getInstance().getTime());

			List<String> servers = client.getChildren().forPath("/bamboo/servers");
			for (String server : servers) {
				System.out.println(server);
			}

			String nodePath = "/bamboo/servers/" + host;
			client.checkExists().creatingParentContainersIfNeeded().forPath(nodePath);
			client.create().orSetData().withMode(CreateMode.EPHEMERAL).forPath(nodePath,
					JsonUtil.toJsonString(status).getBytes("utf-8"));
		} catch (Exception ex) {
			ex.printStackTrace();
			CloseableUtils.closeQuietly(client);
		}
	}
}