package cache.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.tangosol.net.NamedCache;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FlushTag is a JSP tag that is used with the CacheTag. The FlushTag allows you
 * to invalidate a complete cache or a particular cache element identified by
 * the key.
 * 
 * Usage Example: <%@ taglib prefix="ias" uri="http://mascix.com/taglibs/coherence"
 * %> <ias:flush key="<%= cacheKey %>" />
 */
public class FlushTag extends TagSupport {
	private static final long serialVersionUID = -1692316716694992473L;

	/**
	 * The key for the cache entry that needs to be flushed.
	 */
	private String _key;

	/**
	 * This specifies the scope of the cache that needs to be flushed.
	 */
	private int _scope = PageContext.APPLICATION_SCOPE;

	/**
	 * The logger to use for logging ALL web container related messages.
	 */
	private static final Logger _logger = Logger.getLogger(FlushTag.class.getName());

	/**
	 * The resource bundle containing the localized message strings.
	 */
	private static final ResourceBundle _rb = _logger.getResourceBundle();

	// ---------------------------------------------------------------------
	// Tag logic

	/**
	 * doStartTag is called when the flush tag is encountered. By the time this
	 * is called, the tag attributes are already set.
	 * 
	 * @throws JspException
	 *             the standard exception thrown
	 * @return SKIP_BODY since the tag should be empty
	 */
	public int doStartTag() throws JspException {
		// get the cache from the specified scope
		NamedCache cache = CacheUtil.getCache(pageContext, _scope);

		// generate the cache key using the user specified key.

		if (_key != null) {
			String key = CacheUtil.generateKey(_key, pageContext);

			// remove the entry for the key
			cache.remove(key);

			if (_logger.isLoggable(Level.FINE))
				_logger.fine("FlushTag: clear [" + key + "]");
		} else {
			// clear the entire cache
			cache.clear();

			if (_logger.isLoggable(Level.FINE))
				_logger.fine("FlushTag: clear cache");
		}

		return SKIP_BODY;
	}

	/**
	 * doEndTag just resets all the valiables in case the tag is reused
	 * 
	 * @throws JspException
	 *             the standard exception thrown
	 * @return always returns EVAL_PAGE since we want the entire jsp evaluated
	 */
	public int doEndTag() throws JspException {
		_key = null;
		_scope = PageContext.APPLICATION_SCOPE;

		return EVAL_PAGE;
	}

	// ---------------------------------------------------------------------
	// Attribute setters

	/**
	 * This is set a key for the cache element that needs to be cleared
	 */
	public void setKey(String key) {
		if (key != null && key.length() > 0)
			_key = key;
	}

	/**
	 * Sets the scope of the cache.
	 * 
	 * @param scope
	 *            the scope of the cache
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified scope is different from request, session,
	 *             and application
	 */
	public void setScope(String scope) {
		_scope = CacheUtil.convertScope(scope);
	}
}