package dal;

import interfaces.ISessionRepository;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Session;

public class FakeSessionRepository implements ISessionRepository {

	private static Set<Session> sessions = new HashSet<Session>();

	@Override
	public void add(final Session session) {
		sessions.add(session);
	}

	@Override
	public void remove(final Session session) {
		sessions.remove(session);
	}

	@Override
	public Set<Session> list() {
		return sessions;
	}

}
