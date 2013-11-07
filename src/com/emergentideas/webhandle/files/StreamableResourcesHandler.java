package com.emergentideas.webhandle.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.emergentideas.logging.Logger;
import com.emergentideas.logging.SystemOutLogger;
import com.emergentideas.utils.DateUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.Name;
import com.emergentideas.webhandle.exceptions.CouldNotHandle;
import com.emergentideas.webhandle.handlers.Handle;
import com.emergentideas.webhandle.handlers.HttpMethod;
import com.emergentideas.webhandle.output.DirectRespondent;
import com.emergentideas.webhandle.output.Show;
import com.emergentideas.webhandle.output.Template;

/**
 * Serves file and directory resources from a StreamableResourceSource on request by path.
 * @author kolz
 *
 */
public class StreamableResourcesHandler {

	protected StreamableResourceSource source;
	protected Logger log = SystemOutLogger.get(StreamableResourcesHandler.class);
	protected int cacheTime;
	protected boolean showDirectoryContents = false;
	protected int secondsIn5Years = 5 * 365 * 24 * 60 * 60;
	
	public StreamableResourcesHandler(StreamableResourceSource source) {
		this(source, 0);
	}
	
	/**
	 * 
	 * @param source
	 * @param cacheTime The number of seconds to specify before the entry becomes invalid.  
	 * Default is zero causing the client to verify the freshness.
	 */
	public StreamableResourcesHandler(StreamableResourceSource source, int cacheTime) {
		this.source = source;
		this.cacheTime = cacheTime;
	}
	
	@Handle(value = "/{filePath:.+}", method = HttpMethod.GET)
	@Template
	public Object handle(String filePath, ServletContext servletContext, @Name("If-None-Match") String existingETag, Location loc) {
		boolean virtual = isVirtualResource(filePath); 
		if(virtual) {
			filePath = getNonVirtualPath(filePath);
			if(filePath == null) {
				return new CouldNotHandle() {};
			}
		}
		
		Resource resource = source.get(filePath);
		if(resource == null) {
			return new CouldNotHandle() {};
		}
		
		// must-revalidate causes the browser to rigorously adhere to the caching rules without take
		// what the spec refers to as "liberties". It does not, as the name would imply, cause every
		// use of a cached object to be revalidated against the server copy.
		String revalidateSegment = ", must-revalidate";
		
		Calendar c = Calendar.getInstance();
		int effectiveCacheTime = cacheTime;
		if(virtual) {
			// virtual resources are assumed never to be changed so let's give them a long cache time
			c.add(Calendar.YEAR, 5);
			effectiveCacheTime = secondsIn5Years;
		}
		else if(cacheTime > 0) {
			c.add(Calendar.SECOND, cacheTime);
		}
		else {
			// if the cache time is zero we'll push the expire date back an hour to account for any difference
			// between the client's clock and the server clock
			c.add(Calendar.HOUR, -1);
		}

		Map<String, String> headers = new HashMap<String, String>();
		
		if(resource instanceof StreamableResource) {
			headers.put("Content-Type", servletContext.getMimeType(filePath));
			headers.put("Cache-Control" , (effectiveCacheTime > 0 ? "public, " : "no-cache, ") + "max-age=" + effectiveCacheTime + revalidateSegment);
			headers.put("Expires", DateUtils.htmlExpiresDateFormat().format(c.getTime()));
			StreamableResource sr = (StreamableResource)resource;
			headers.put("ETag", sr.getEtag());
			if(sr.getEtag().equals(trimETag(existingETag))) {
				return new DirectRespondent(null, 304, headers);
			}
			
			try {
				return new DirectRespondent(sr.getContent(), 200, headers);
			}
			catch(Exception e) {
				log.error("Could not serve content for path: " + filePath, e);
				throw new RuntimeException(e);
			}
		}
		else if(showDirectoryContents && resource instanceof Directory) {
			if(filePath.endsWith("/") == false) {
				if(isAbsoluteFilePath(filePath) == false) {
					filePath = "/" + filePath;
				}
				return new Show(filePath + "/");
			}
			Directory dir = (Directory)resource;
			List<String> entries = new ArrayList<String>();
			for(Resource r : dir.getEntries()) {
				if(r instanceof NamedResource) {
					String name = ((NamedResource)r).getName();
					if(r instanceof Directory) {
						if(name.endsWith("/") == false) {
							name += "/";
						}
					}
					
					entries.add(name);
				}
			}
			loc.put("entries", entries);
			loc.put("directoryLocation", filePath);
			return "filetemplates/directoryIndex";
		}
		
		return new CouldNotHandle() {};
	}
	
	protected boolean isAbsoluteFilePath(String path) {
		if(File.pathSeparatorChar == '/') {
			return path.startsWith("/");
		}
		else {
			if(path == null || path.length() < 4) {
				return false;
			}
			if(Character.isLetter(path.charAt(0)) && path.charAt(1) == ':' && path.charAt(2) == '\\' ) {
				return true;
			}
			return false;
		}
	}
	
	protected String trimETag(String eTag) {
		if(eTag == null) {
			return null;
		}
		
		int i = eTag.indexOf('"');
		if(i >= 0) {
			eTag = eTag.substring(i + 1);
		}
		
		i = eTag.lastIndexOf('"');
		if(i >= 0) {
			eTag = eTag.substring(0, i);
		}
		
		return eTag;
	}
	
	protected boolean isVirtualResource(String filePath) {
		return filePath.startsWith("vrsc/");
	}
	
	protected String getNonVirtualPath(String filePath) {
		int length = filePath.length();
		for(int i = getVitualFixedOffset(filePath); i < length; i++) {
			if(filePath.charAt(i) == '/') {
				if(i < length - 1) {
					return filePath.substring(i + 1);
				}
				break;
			}
		}
		
		return null;
	}
	
	protected int getVitualFixedOffset(String filePath) {
		return 5;
	}

	public int getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}

	public boolean isShowDirectoryContents() {
		return showDirectoryContents;
	}

	public void setShowDirectoryContents(boolean showDirectoryContents) {
		this.showDirectoryContents = showDirectoryContents;
	}

	public StreamableResourceSource getSource() {
		return source;
	}

	public void setSource(StreamableResourceSource source) {
		this.source = source;
	}
	
}
