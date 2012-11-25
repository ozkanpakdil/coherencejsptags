package cache.jsp;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class CacheContextListener implements ServletContextListener {
	/**
	 * Public constructor taking no arguments according to servlet spec
	 */
	public CacheContextListener() {
	}

	/**
	 * This is called when the context is created.
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		// creates jsp cache in the coherence cluster
		NamedCache cache = CacheFactory.getCache("mascix.jsp.cache");

		// set the cache as a context attribute
		if (cache != null)
			context.setAttribute(Constants.JSPTAG_CACHE_KEY, cache);
	}

	/**
	 * This is called when the context is shutdown.
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		// Remove the cache from context and clear the cache
		NamedCache cache = (NamedCache) context.getAttribute(Constants.JSPTAG_CACHE_KEY);

		if (cache != null) {
			context.removeAttribute(Constants.JSPTAG_CACHE_KEY);
			cache.clear();
		}
	}
}