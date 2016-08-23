package luke.bamboo.client;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 计算时间消耗
 * 
 * @author luke
 */
public class TimerCounter {
	private static final Logger logger = LoggerFactory.getLogger(TimerCounter.class);
    List<Long> startTime = new ArrayList<Long>();
    long average;
    int averageCount;
    long max;
    long min = Long.MAX_VALUE;
    
    public void addStartTime(long st) {
    	startTime.add(st);
    }
    
    public void addStartTime() {
    	addStartTime(System.currentTimeMillis());
    }
    
    public long getSpendTime(long et) {
    	long t = et - startTime.remove(0);
    	average += t;
    	averageCount++;
    	if(t > max)
    		max = t;
    	if(t < min)
    		min = t;
    	return t;
    }
    
    public long getSpendTime() {
    	return getSpendTime(System.currentTimeMillis());
    }
    
    public float getAverage() {
    	return average / averageCount;
    }

	public long getMax() {
		return max;
	}

	public long getMin() {
		return min;
	}
	
	public int getAverageCount() {
		return averageCount;
	}
	
	public void printReport() {
		logger.info(String.format("average time: %dms, count: %d, max: %dms, min: %dms", average, averageCount, max, min));
	}
	
	public static void printReport(List<TimerCounter> list) {
		long total = 0;
		long max = 0;
		long min = Long.MAX_VALUE;
		for(TimerCounter t : list) {
			total += t.getAverage();
			if(t.getMax() > max)
				max = t.getMax();
			if(t.getMin() < min)
				min = t.getMin();
		}
		System.out.println(String.format("average time: %dms, total time:%d, count: %d, max: %dms, min: %dms", total/list.size(), total, list.size()*list.get(0).getAverageCount(), max, min));
		logger.info(String.format("average time: %dms, total time:%d, count: %d, max: %dms, min: %dms", total/list.size(), total, list.size()*list.get(0).getAverageCount(), max, min));
	}
}
