package chosema.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public abstract class AbstractController implements Serializable {
	private static final long serialVersionUID = 1L;

	protected final Logger LOGGER = Logger.getLogger(this.getClass());

	public static final String TITLE_INFO = "Information";
	public static final String TITLE_WARN = "Warning";
	public static final String TITLE_ERROR = "Error";

	protected String getParam(final String param) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(param);
	}

	protected Map<String, String> getParams() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}

	protected boolean isPostback() {
		return FacesContext.getCurrentInstance().isPostback();
	}

	protected boolean isNotPostback() {
		return !isPostback();
	}

	protected void showMessage(final String clientId, final boolean keepMessages, final FacesMessage.Severity severity, final String title, final String message) {
		final FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(keepMessages);
		context.addMessage(clientId, new FacesMessage(severity, title, message));
	}

	protected void showInfoMessage(final String message) {
		showInfoMessage(false, message);
	}

	protected void showInfoMessage(final boolean keepMessages, final String message) {
		showInfoMessage(null, keepMessages, message);
	}

	protected void showInfoMessage(final String clientId, final boolean keepMessages, final String message) {
		showMessage(clientId, keepMessages, FacesMessage.SEVERITY_INFO, TITLE_INFO, message);
	}

	protected void showWarnMessage(final String message) {
		showWarnMessage(false, message);
	}

	protected void showWarnMessage(final boolean keepMessages, final String message) {
		showWarnMessage(null, keepMessages, message);
	}

	protected void showWarnMessage(final String clientId, final boolean keepMessages, final String message) {
		showMessage(clientId, keepMessages, FacesMessage.SEVERITY_WARN, TITLE_WARN, message);
	}

	protected void showErrorMessage(final String message) {
		showErrorMessage(false, message);
	}

	protected void showErrorMessage(final boolean keepMessages, final String message) {
		showErrorMessage(null, keepMessages, message);
	}

	protected void showErrorMessage(final String clientId, final boolean keepMessages, final String message) {
		showMessage(clientId, keepMessages, FacesMessage.SEVERITY_ERROR, TITLE_ERROR, message);
	}

	protected void validationFailed(final String message) {
		validationFailed(null, message);
	}

	protected void validationFailed(final String clientId, final String message) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getExternalContext().getFlash().setKeepMessages(false);
		facesContext.addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, TITLE_ERROR, message));
		facesContext.validationFailed();
	}

	protected void returnForbidden(final String message) {
		sendResponse(HttpServletResponse.SC_FORBIDDEN, message);
	}

	protected void returnInternalServerError(final String message) {
		sendResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
	}

	protected void returnNotFound(final String message) {
		sendResponse(HttpServletResponse.SC_NOT_FOUND, message);
	}

	protected void sendResponse(final int response, final String message) {
		try {
			final FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().responseSendError(response, message);
			facesContext.responseComplete();
		} catch (final IOException e) {
			LOGGER.error(e, e);
		}
	}

}
