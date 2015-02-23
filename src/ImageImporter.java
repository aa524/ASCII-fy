import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.awt.image.*;
import java.lang.Exception;

import javax.imageio.*;
public class ImageImporter {

	public static void main(String[] args) throws Exception {
		File file= new File(ImageImporter.class.getResource("image.png").toURI());
		ImageImporter.toASCII(file);
//		ImageImporter.toGrayscale(file);

	}


	//Assume image is already in grayscale and only has 3 bands and at least bigger than 8x8
	public static void toASCII(File image) throws IOException, Exception{
		System.out.println(image.getName());
		BufferedImage img= ImageIO.read(image);
		WritableRaster raster= (WritableRaster) img.getData();
		System.out.println(img.getColorModel());
		System.out.println(raster.getNumBands());
		System.out.println(raster.getWidth());
		System.out.println(raster.getHeight());
		CharacterImporter.arrayOfPixels();
		
		//Character ramp from http://paulbourke.net/dataformats/asciiart/
		// .:-=+*#%@
		char[] charRamp= {'^', '.', ':', '-', '=','+', '*', '#', '%', '@', ';', '`', ' ', '~'};
		int[] diffs=new int[charRamp.length];
		int[][] charmapsfixed= new int[charRamp.length][raster.getNumBands()*64];
		if (raster.getNumBands()==3) {
		for (int k=0; k< charRamp.length; k++) {
			int count1=0;
			//x will have a value of 0 or 1
			//Resizing array from charArray to appropriate size (3x original size)
			for (int x: CharacterImporter.charArray.get(charRamp[k])) {
				charmapsfixed[k][count1]=x*255;
				charmapsfixed[k][count1 + 1]=x*255;
				charmapsfixed[k][count1 + 2]=x*255;
				count1=count1+3;
			}
		}
		}
		//handling images with alpha band
		else if (raster.getNumBands()==4) {
			for (int k=0; k< charRamp.length; k++) {
				int count1=0;
				//x will have a value of 0 or 1
				//Resizing array from charArray to appropriate size (4x original size)
				for (int x: CharacterImporter.charArray.get(charRamp[k])) {
					charmapsfixed[k][count1]=x*255;
					charmapsfixed[k][count1 + 1]=x*255;
					charmapsfixed[k][count1 + 2]=x*255;
					//alpha band
					charmapsfixed[k][count1 + 3]=255;
					count1=count1+4;
				}
			}
			
		}

		for (int i=0; i< raster.getWidth()/8; i++) {
			for (int j=0; j < raster.getHeight()/8; j++) {
				int[] holder= null;
				holder= raster.getPixels(i*8, j*8, 8, 8, holder);
				int[] holder1band= new int[64];
				int count=0, index=0;
				int threshold=128;
				for (int x: holder) {
					if (count%(raster.getNumBands())==2){
						if (x > threshold) {
							holder1band[index]=0;}
						else {
							holder1band[index]=1;}
						index++;}
					count++;}
				for (int k=0; k< diffs.length; k++) {
					diffs[k]= ImageImporter.comparison(holder1band, CharacterImporter.charArray.get(charRamp[k]));
				}
				int minimum=Integer.MAX_VALUE;
				int minIndex=0;
				for (int p=0; p< diffs.length; p++) {
					if (diffs[p] < minimum) {
						minimum=diffs[p];
						minIndex=p;
					}
				}
				raster.setPixels(i*8, j*8, 8, 8, charmapsfixed[minIndex]);
				img.setData(raster);
			}
		}
		String fullfilename= image.getName();
		String filetype = fullfilename.substring(fullfilename.lastIndexOf(".") + 1, fullfilename.length());
		String filename= fullfilename.substring(0, fullfilename.lastIndexOf("."));
		File destination= new File("C:\\Users\\Arun\\Desktop\\hello1\\" + filename + "ASCII." + filetype);
		ImageIO.write(img, filetype, destination);




	}



	public static void toGrayscale(File image) throws IOException{
		BufferedImage img= ImageIO.read(image);
		WritableRaster raster= (WritableRaster) img.getData();
		System.out.println(raster.getNumBands());
		System.out.println(raster.getWidth());
		System.out.println(raster.getHeight());
		int[] holder=null;
		holder= raster.getPixels(0, 0, raster.getWidth(), raster.getHeight(), holder);
		System.out.println(holder.length);
		int[] grayscale= new int[holder.length];
		int index=0;
		int nBands= raster.getNumBands();
		for (int i: holder) {
			if (index%nBands==0) {
				grayscale[(index/nBands)*nBands]+=(int) (0.21*i);
				grayscale[(index/nBands)*nBands + 1]+=(int) (0.21*i);
				grayscale[(index/nBands)*nBands + 2]+=(int) (0.21*i);
			}
			else if (index%nBands==1) {
				grayscale[(index/nBands)*nBands]+=(int) (0.72*i);
				grayscale[(index/nBands)*nBands + 1]+=(int) (0.72*i);
				grayscale[(index/nBands)*nBands + 2]+=(int) (0.72*i);

			}
			else if (index%3==2) {
				grayscale[(index/nBands)*nBands]+=(int) (0.07*i);
				grayscale[(index/nBands)*nBands + 1]+=(int) (0.07*i);
				grayscale[(index/nBands)*nBands + 2]+=(int) (0.07*i);
			}
			index++;
		}

		//use this for the writableraster
		raster.setPixels(0, 0, raster.getWidth(), raster.getHeight(), grayscale);
		img.setData(raster);
		File destination= new File("C:\\Users\\Arun\\Desktop\\hello1\\try4.jpg");
		ImageIO.write(img, "jpg", destination);



	}

	//compares two bit arrays of size 8x8 and determines the difference between them
	//maybe should be comparing raster with boolean array
	//difference of 0 means both arrays are the same
	//one array will be the map of a character, the other a portion of the image
	//true means value of 1 (or black pixel), false means value of 0 (or white pixel)
	public static int comparison(boolean[][] a, boolean[][] b) throws Exception {
		//check for same size
		if (a.length != b.length || a[0].length != b[0].length) {
			throw new Exception("Arrays not the same size");
		}
		int difference=0;
		//assume 2d arrays are rectangular in shape and a and b are the same size
		for (int y=0; y < a.length; y++) {
			for (int x=0; x < a[0].length; x++ ) {
				if (a[y][x] != b[y][x]) {
					difference++;
				}
			}
		}
		return difference;
	}

	//compares two int arrays of size 64 and determines the difference between them
	//maybe should be comparing raster with boolean array
	//difference of 0 means both arrays are the same
	//true means value of 1 (or black pixel), false means value of 0 (or white pixel)
	public static int comparison(int[] a, int[] b) throws Exception {
		//check for same size
		if (a.length != b.length) {
			throw new Exception("Arrays not the same size");
		}
		int difference=0;
		for (int y=0; y < a.length; y++) {
			if (a[y]!= b[y]) {
				difference++;
			}
		}
		return difference;
	}


	//From http://www.johndcook.com/blog/2009/08/24/algorithms-convert-color-grayscale/
	//Using Luminosity method
	private static int RGBtoGrayscale(int r, int g, int b) {
		return (int) (0.21*r + 0.72*g + 0.07*b);
	}


}
