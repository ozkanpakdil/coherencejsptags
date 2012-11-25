package cache.jsp;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import com.tangosol.net.NamedCache;

public class CacheUtil {

	/**
	 * The resource bundle containing the localized message strings.
	 */
	private static final Logger logger = Logger.getLogger(CacheUtil.class.getName());

	private static final ResourceBundle _rb = logger.getResourceBundle();

	private static final String PAGE_SCOPE = "page";
	private static final String REQUEST_SCOPE = "request";
	private static final String SESSION_SCOPE = "session";
	private static final String APPLICATION_SCOPE = "application";

	/**
	 * This is used to get the cache itself. The cache is stored as an attribute
	 * in the specified scope.
	 * 
	 * @return the cache object
	 */
	public static NamedCache getCache(PageContext pc, int scope) {
		return (NamedCache) pc.getAttribute(Constants.JSPTAG_CACHE_KEY, scope);
	}

	/**
	 * This function generates the key to the cache. It creates the key by
	 * suffixing the servlet path with either the user-specified key or by
	 * keeping a counter in the request attribute which it will increment each
	 * time so that multiple cache tags in a page each get a unique key.
	 * 
	 * @return the generated key
	 */
	public static String generateKey(String key, PageContext pc) {
		HttpServletRequest req = (HttpServletRequest) pc.getRequest();

		// use the key as the suffix by default
		String suffix = key;
		if (suffix == null) {
			String saved = (String) req.getAttribute(Constants.JSPTAG_COUNTER_KEY);

			if (saved == null)
				suffix = "1";
			else
				suffix = Integer.toString(Integer.parseInt(saved) + 1);

			req.setAttribute(Constants.JSPTAG_COUNTER_KEY, suffix);
		}

		// concatenate the servlet path and the suffix to generate key
		return req.getServletPath() + '_' + suffix;
	}

	/*
	 * Converts the string representation of the given scope into an int.
	 * 
	 * @param scope The string representation of the scope
	 * 
	 * @return The corresponding int constant
	 * 
	 * @throws IllegalArgumentException if the specified scope is different from
	 * request, session, and application
	 */
	public static int convertScope(String scope) {

		int ret;

		if (REQUEST_SCOPE.equalsIgnoreCase(scope)) {
			ret = PageContext.REQUEST_SCOPE;
		} else if (SESSION_SCOPE.equalsIgnoreCase(scope)) {
			ret = PageContext.SESSION_SCOPE;
		} else if (APPLICATION_SCOPE.equalsIgnoreCase(scope)) {
			ret = PageContext.APPLICATION_SCOPE;
		} else if (PAGE_SCOPE.equalsIgnoreCase(scope)) {
			ret = PageContext.PAGE_SCOPE;
		} else {
			String msg = _rb.getString("taglibs.cache.illegalScope");
			msg = MessageFormat.format(msg, new Object[] { scope });
			throw new IllegalArgumentException(msg);
		}

		return ret;
	}

}
