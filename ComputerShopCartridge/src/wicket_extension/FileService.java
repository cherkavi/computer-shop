package wicket_extension;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;

public class FileService {
    
    private File file;
    private String outputName;
    private String contentType = "";
    
    public FileService(File file) {
            this.file = file;
            this.outputName = this.file.getName();
    }
    
    /**
     * sets the output name and returns itself
     * @param outputName
     * @return
     */
    public FileService setOutputName(String outputName) {
            this.outputName = outputName;
            return FileService.this;
    }
    
    /**
     * sets the content type and returns itself
     * @param contentType
     * @return
     */
    public FileService setContentType(String contentType) {
            this.contentType = contentType;
            return FileService.this;
    }
    
    
    public IResourceStream getResourceStream() throws IOException {
            
            FileInputStream fi = new FileInputStream(this.file);

            return new IResourceStreamImpl(fi, this.contentType, 
            	this.file.length());
    }
    
    /**
     * wrapper which creates the necessary [EMAIL PROTECTED] ResourceStreamRequestTarget}
     * @return
     * @throws IOException
     */

    public ResourceStreamRequestTarget getResourceStreamRequestTarget() throws IOException {
            return new ResourceStreamRequestTarget(this.getResourceStream()) {
                    public String getFileName() {
                            return (outputName);
                    }
            };
    }
}


class IResourceStreamImpl implements IResourceStream {
    private static final long serialVersionUID = 1L;

    private Locale locale = null;
    private String contentType = null;
    private InputStream inputStream = null;
    private long size;

    /**
     * @param fileInputStream
     * @param contentType
     * @param file
     */
    public IResourceStreamImpl(InputStream inputStream,
                    String contentType, long size) {
            this.inputStream = inputStream;
            this.size = size;
            this.contentType = contentType;
    }

    public void close() throws IOException {
            this.inputStream.close();
    }


public InputStream getInputStream() throws ResourceStreamNotFoundException {

            return this.inputStream;
    }

    public String getContentType() {
            return (this.contentType);
    }

    public Locale getLocale() {
            return (this.locale);
    }

    public long length() {
            return this.size;
    }

    public void setLocale(Locale locale) {
            this.locale = locale;
    }

    public Time lastModifiedTime() {
            return null;
    }

}
