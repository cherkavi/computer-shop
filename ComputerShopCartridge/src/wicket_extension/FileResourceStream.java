package wicket_extension;

import java.io.File;
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
	private transient InputStream inputStream;
	private File file=null;
	
	public FileResourceStream(String path){
		System.out.println("Path to File: "+path);
		try{
			file=new File(path);
		}catch(Exception ex){
			System.err.println("FileResourceStream Exception:"+ex.getMessage());
			file=null;
		};
		
		this.pathToFile=path;
	}
	
	@Override
	public void close() throws IOException {
		if(this.inputStream!=null){
			this.inputStream.close();
			this.inputStream=null;
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
	
	@Override
	public long length() {
		if(this.file!=null){
			return file.length();
		}else {
			return 0;
		}
	}

}
