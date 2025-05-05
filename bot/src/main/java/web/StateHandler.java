package web;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class StateHandler {

    private final ConcurrentMap<String, Boolean> activityMap = new ConcurrentHashMap<>();

    public void addActive(final String id) {
        activityMap.put(id, true);
    }

    public void deleteActive(final String id) {
        activityMap.remove(id);
    }

    public Boolean isActive(final String id) {
        return activityMap.getOrDefault(id, false);
    }
}
