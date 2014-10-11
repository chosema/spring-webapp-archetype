package chosema.web;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class TransientViewScope implements Scope {

	private static final String VIEW_SCOPE_ATTR = TransientViewScope.class.getName();

	@Override
	public Object get(final String name, final ObjectFactory<?> objectFactory) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
		final Map<String, Object> viewMap = getSessionViewMap(facesContext);

		final Object createdObject;
		if (facesContext.isPostback() || requestMap.containsKey(VIEW_SCOPE_ATTR)) {
			if (viewMap.containsKey(name)) {
				createdObject = viewMap.get(name);
			} else {
				createdObject = createObject(name, objectFactory, viewMap);
			}
		} else {
			viewMap.clear();
			createdObject = createObject(name, objectFactory, viewMap);
		}

		requestMap.put(VIEW_SCOPE_ATTR, VIEW_SCOPE_ATTR);

		return createdObject;
	}

	@Override
	public Object remove(final String name) {
		return getSessionViewMap(FacesContext.getCurrentInstance()).remove(name);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getSessionViewMap(final FacesContext facesContext) {
		final HttpSession session = ((HttpServletRequest) facesContext.getExternalContext().getRequest()).getSession();
		Object viewMap = session.getAttribute(VIEW_SCOPE_ATTR);
		if (viewMap == null) {
			viewMap = new HashMap<String, Object>();
			session.setAttribute(VIEW_SCOPE_ATTR, viewMap);
		}
		return (Map<String, Object>) viewMap;
	}

	private Object createObject(final String name, final ObjectFactory<?> objectFactory, final Map<String, Object> viewMap) {
		final Object object = objectFactory.getObject();
		viewMap.put(name, object);
		return object;
	}

	@Override
	public void registerDestructionCallback(final String name, final Runnable callback) { // Not supported
	}

	@Override
	public Object resolveContextualObject(final String key) {
		return null;
	}

	@Override
	public String getConversationId() {
		return null;
	}

}
