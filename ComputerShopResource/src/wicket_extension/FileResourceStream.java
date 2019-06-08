package wicket_extension;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/** ресурс, который содержит данные из файла на локальном диске */
public class FileResourceStream extends AbstractResourceStream{
	private final static long serialVersionUID=1L;
	/** полный путь к локальному файлу */
	private String pathToFile;
	/** */
	private InputStream inputStream;
	
	public FileResourceStream(String path){
		this.pathToFile=path;
	}
	
	@Override
	public void close() throws IOException {
		if(this.inputStream!=null){
			this.inputStream.close();
		}
	}

	@Override
	public InputStream getInputStream() throws ResourceStreamNotFoundException {
		try{
			this.inputStream=new FileInputStream(this.pathToFile);
			return this.inputStream;
		}catch(Exception ex){
			return null;
		}
		
	}

}
