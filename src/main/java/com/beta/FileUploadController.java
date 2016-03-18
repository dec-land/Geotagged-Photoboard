package com.beta;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

//Create a spring boot controller which is automatically detected by spring boot, used for mapping methods to different urls
@Controller
public class FileUploadController {
	
	GeoLocation geoLocation;
	@Autowired
	PhotoRepository repository;
	@Autowired
	HttpServletRequest request;
	private static final Logger log = Logger.getLogger(FileUploadController.class.getName());
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	//Open the index template (index.html) whenever a get method to / or /geotagged is called.
	@RequestMapping(value = { "/", "/geotagged" }, method = RequestMethod.GET )
	String geotagged() {
		return "index";
	}

	/*
	 	Runs this method whenever a post method is sent to uploaded, used for when a image is uploaded.
	 	Gets the values supplied in the form using the RequestParam annotation.
	*/
	
	@RequestMapping(value = { "/geotagged/uploaded", "/uploaded" }, method = RequestMethod.POST )
	public String handleFileUpload(@RequestParam("desc") String desc, @RequestParam("file") MultipartFile file,
			@RequestParam("status") int status, @RequestParam("eventSelection") String event) {

		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				
				Date date = new Date();

				//Get the metadata from the bytes of the image.
				Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(bytes));
				
				//If the image contains gps data and date
				if (metadata.containsDirectoryOfType(GpsDirectory.class)) {
					
					//Get the gps directory and then get the geolocation object from this which can be used to find lat and long.
					Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
					for (GpsDirectory gpsDirectory : gpsDirectories) {
						this.geoLocation = gpsDirectory.getGeoLocation();
					}
					
					//Get the data from the metadata.
					ExifSubIFDDirectory directory = (ExifSubIFDDirectory) metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
					date = directory.getDate(36867);

					double lat = this.geoLocation.getLatitude();
					double lon = this.geoLocation.getLongitude();

					//Get the path to image using the http request from the upload form and then store the image on the server.
					String uploadsDir = "/";
					String realPathtoUploads = this.request.getServletContext().getRealPath(uploadsDir);

					String filePath = realPathtoUploads + "WEB-INF/classes/static/images/photo"
							+ this.repository.count() + ".jpg";
					File dest = new File(filePath);
					file.transferTo(dest);

					//Path which is stored in the database, relative to the web server.
					String path = "/geotagged/images/photo" + this.repository.count() + ".jpg";

					String dateString = this.df.format(date);
					
					//Save the image into the repository(database).
					this.repository.save(new Photo(desc, lat, lon, path, dateString, status, event,1));
					
					//Open the index page
					return "index";
				}
				
				//Open the selection page where lat and long can be selected.
				
				
				Date selectDate = new Date();

				String uploadsDir = "/";
				String realPathtoUploads = this.request.getServletContext().getRealPath(uploadsDir);

				String filePath = realPathtoUploads + "WEB-INF/classes/static/images/photo" + this.repository.count()
						+ ".jpg";
				File dest = new File(filePath);
				file.transferTo(dest);

				String path = "/geotagged/images/photo" + this.repository.count() + ".jpg";

				String dateString = this.df.format(selectDate);
				
				this.repository.save(new Photo(desc,0, 0, path, dateString, status, event,0));
				return "select";

			} catch (Exception e) {
				log.info(e.toString());
				// Open the error page.
				return "error";
			}
		}
		log.info("no file");
		return "error";
	}

	/*
	 	Runs this method whenever a post method is sent to select,used for when lat and long are selected on the map.
	 	Gets the values supplied in the form using the RequestParam annotation.
	 	Pretty much the same as the other method without getting the lat and long from the image.
	*/
	@RequestMapping(value = { "/geotagged/select", "/select" }, method = RequestMethod.POST)
	public String handleSelectUpload(@RequestParam("lng") double lng, @RequestParam("lat") double lat) {
		
		try {

			List<Photo> photo = repository.findByMeta(0);
			photo.get(0).setLat(lat);
			photo.get(0).setLon(lng);
			photo.get(0).setMeta(1);
			this.repository.save(photo);
			
			return "index";
		} catch (Exception e) {
			log.info(e.toString());
			return "error";
		}
	}
}
