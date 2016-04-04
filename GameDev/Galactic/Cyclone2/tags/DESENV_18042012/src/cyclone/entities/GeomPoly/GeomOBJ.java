package cyclone.entities.GeomPoly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import cristiano.comum.Formatacao;
import cristiano.math.Vector3;
import cyclone.Cyclone;
import cyclone.CycloneConfig;
import cyclone.entities.geom.Geom;
import cyclone.entities.geom.GeomBox;

/**
 * The data that has been read from the Wavefront .obj file. This is
 * kept seperate from the actual rendering with the hope the data
 * might be used for some other rendering engine in the future.
 * 
 * @author Kevin Glass
 */
public class GeomOBJ extends GeomBox{
	
    private int nPontos=CycloneConfig.getInternalPointsEixo();
    
	
	/** The verticies that have been read from the file */
	private ArrayList<Vector3> verts = new ArrayList<Vector3>();
	/** The normals that have been read from the file */
	//private ArrayList<Vector3> normals = new ArrayList<Vector3>();
	/** The texture coordinates that have been read from the file */
	//private ArrayList<Tuple2> texCoords = new ArrayList<Tuple2>();
	/** The faces data read from the file */
//	private ArrayList<Face> faces = new ArrayList<Face>();
	private ArrayList<Face> indices = new ArrayList<Face>();
	
	//Object3dList hull;
	
	
	private ArrayList<InternalPoint> points = new ArrayList<InternalPoint>(); //Internal Points
	

	public Vector3 minPoint;
	public Vector3 maxPoint;
	float scale;
	/**
	 * Create a new set of OBJ data by reading it in from the specified
	 * input stream.
	 * @param scale 
	 * 
	 * @param in The input stream from which to read the OBJ data
	 * @throws IOException Indicates a failure to read from the stream
	 */

	public GeomOBJ(String fileName, float scale,double mass){
		//super(PrimitiveType.POLYGON);
		super(new Vector3(1,1,1),1);
		setType(Geom.PrimitiveType.POLYGON);
		
		this.scale=scale;
		minPoint=new Vector3(999999,999999,999999);
		maxPoint=new Vector3(-999999,-999999,-999999);
		URL model=null;
		try {
			model = new URL(fileName);
		} catch (MalformedURLException e1) {
			Cyclone.log(e1.getMessage());
		}
		if (model!=null){
			try {
				int verts=loadModel(model.openStream());
				
				System.out.println(fileName+ " contains "+ verts+ " vertices");
				//generateHull();
			} catch (IOException e) {
				Cyclone.log(e.getMessage());
			}
			maxPoint.multiVectorScalar(1.1);
			minPoint.multiVectorScalar(1.1);
			halfSize=(maxPoint.getSubVector(minPoint).getMultiVector(0.5));
			
			
		}
		
		generateInternalPoints();
		linkaFaceInternalPoint();
		
		
		Vector3 ipSize=halfSize.getMultiVector(1.0/getnPontos()*2);
		
		setDensity(mass/ipSize.magnitude() / points.size());
		
		System.out.println("Pontos Internos encontrados:"+points.size()+" Massa(kg):"+Formatacao.formatMass(calculateMass())+" IPSize:"+Formatacao.format(ipSize.magnitude()));
	}
	
	



	public InternalPoint getIP(int i){
		return points.get(i);
	}
	private void linkaFaceInternalPoint() {
		linkNearestFaceToIP();
		
		for (int i=points.size()-1;i>=0;i--){
			InternalPoint ip=points.get(i);
			if (ip.facesCount()==0){
				points.remove(i);}
		}
		//System.out.println("Nova quantidade:"+points.size());
		/*for (int i=points.size()-1;i>=0;i--){
			InternalPoint ip=points.get(i);
			if (!ip.isInternal(ip.getFaces()))
				points.remove(i);
		}*/
		
		//System.out.println("Nova quantidade2:"+points.size());
		linkNearestFaceToIP();	
		
		//Deixar o ponto mais proximo do centro
		/*for (int i=points.size()-1;i>=0;i--){
			InternalPoint ip=points.get(i);
			Vector3 pos=ip.getPosicao();íc
			pos.x=pos.x*0.8;
			pos.y=pos.y*0.8;
			pos.z=pos.z*0.8;
		}*/
	}

	private void linkNearestFaceToIP() {
		for (int i=0;i<points.size();i++){
			InternalPoint ip=points.get(i);
			ip.getFaces().clear();
		}
			
		for (int f=0;f<indices.size();f++){
			Face face=indices.get(f);
			
			double d=-1;
			Vector3 pf=face.getCentral();
			InternalPoint pontoNear=null;
			
			for (int i=0;i<points.size();i++){
				InternalPoint ip=points.get(i);
				double dist=ip.getPosicao().getSubVector(pf).magnitude();
				if ((dist<d) || (d==-1)){
					d=dist;
					pontoNear=ip;
				}
			}
			pontoNear.addFace(face);
		}
	}
	private void generateInternalPoints() {
		//Vector3 dif=maxPoint.subtract(minPoint);
		
		
		for (int x=0;x<nPontos;x++){
			for (int y=0;y<nPontos;y++){
				for (int z=0;z<nPontos;z++){
					//System.out.println(x+","+y+","+z);
					
					InternalPoint ip=new InternalPoint(halfSize.x*2/nPontos*x+minPoint.x,halfSize.y*2/nPontos*y+minPoint.y,halfSize.z*2/nPontos*z+minPoint.z,this);
					//InternalPoint ip=new InternalPoint(dif.x/p*x+minPoint.x,dif.y/p*y+minPoint.y,dif.z/p*z+minPoint.z,this);
					//if (ip.isInternal(indices))
						points.add(ip);
				}
			}
		}
		
		
	}

	/*
	 * Essa função vai converter para um formato que o projeto QuickHull entenda
	 * e gerar um objeto de modo que o cyclone entenda  
	*/
	/*
	private void generateHull(){
		
	    HullAlgorithm algorithm;

	    Point3dObject3d[] points = new Point3dObject3d[verts.size()];
	    for (int i=0; i <verts.size(); i++) {
	      points[i] = new Point3dObject3d(new Point3d(verts.get(i).x,verts.get(i).y,verts.get(i).z));
	    }	    
	    
	    if (hull_method.equals(HULL_INCREMENTAL)) {
	      algorithm = new Incremental(points);
	    } else if (hull_method.equals(HULL_GIFT_WRAP)) {
	      algorithm = new GiftWrap(points);
	    } else if (hull_method.equals(HULL_DIVIDE_CONQUER)) {
	      algorithm = new DivideAndConquer(points);
	    } else if (hull_method.equals(HULL_QUICK_HULL)) {
	      algorithm = new QuickHull(points);
	    } else {
	      algorithm = new Incremental(points); //should not get here
	    }
	      hull = algorithm.build();
	     // Object3d obj3d=hull.elementAt(hull.size()-1);
	    //  System.out.println("ConvexHull ("+hull_method+"): " + hull.size()+" faces");
	      

	}*/

	/*public GeomOBJ(InputStream in) throws IOException {
		loadModel(in);
	}*/

/*A massa dos objetos é baseada na quantidade de pontos internos*/	
	public double calculateMass(){
		Vector3 ipSize=halfSize.getMultiVector(1.0/getnPontos()*2);
		double r=ipSize.magnitude() * getDensity()*points.size();
	//	System.out.println("Super.calculateMass:"+super.calculateMass()+" novo:"+r);
		return r;
	}

	private int loadModel(InputStream in) throws IOException {
		// read the file line by line adding the data to the appropriate
		// list held locally
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		while (reader.ready()) {
			String line = reader.readLine();
			
			// if we read a null line thats means on some systems
			// we've reached the end of the file, hence we want to 
			// to jump out of the loop
			if (line == null) {
				break;
			}
			
			// "vn" indicates normal data
			//if (line.startsWith("vn")) {
			//	Vector3 normal = readVector3(line);
		//		System.out.println(normal.getX()+"f,"+normal.getY()+"f,"+normal.getZ()+"f,");
			//	normals.add(normal);
			// "vt" indicates texture coordinate data
			// if (line.startsWith("vt")) {
				//Tuple2 tex = readTuple2(line);
			//	texCoords.add(tex);
			// "v" indicates vertex data
			//} else 
				if (line.startsWith("v")) {
				Vector3 vert = readVector3(line);
				
				if (vert.getX()<minPoint.x)minPoint.x=vert.getX();
				if (vert.getX()>maxPoint.x)maxPoint.x=vert.getX();
				if (vert.getY()<minPoint.y)minPoint.y=vert.getY();
				if (vert.getY()>maxPoint.y)maxPoint.y=vert.getY();			
				if (vert.getZ()<minPoint.z)minPoint.z=vert.getZ();
				if (vert.getZ()>maxPoint.z)maxPoint.z=vert.getZ();	
				verts.add(vert);
			// "f" indicates a face
			} else if (line.startsWith("f")) {
				readFace(line);
				//if (face!=null)
			//		faces.add(face);
			}
		}
		
		// Print some diagnositics data so we can see whats happening
		// while testing
	//	System.out.println("Read " + verts.size() + " verticies");
	//	System.out.println("Read " + faces.size() + " faces");
		return verts.size();
	}
	
	public double[] getVertices(){
		double[] ret=new double[+verts.size()*3];//normals.size()*3];
		int tot=0;
		for (int i=0;i<verts.size();i++){
			ret[tot+0]=verts.get(i).getX();
			ret[tot+1]=verts.get(i).getY();
			ret[tot+2]=verts.get(i).getZ();
					
			tot=tot+3;
		}
		
		return ret;
	}
	
	/*
	 * Esse método vai retornar um vetor[3] dos pontos que compôem a
	 */
	public Face getFace(int i){
		return indices.get(i);
	}
	
	/**
	 * Get the number of faces found in the model file
	 * 
	 * @return The number of faces found in the model file
	 */
	/*public int getFaceCount() {
		return faces.size();
	}*/
	
	/**
	 * Get the data for specific face
	 * 
	 * @param index The index of the face whose data should be retrieved
	 * @return The face data requested
 	 */
	/*public Face getFace(int index) {
		return (Face) faces.get(index);
	}*/
	
	/**
	 * Read a set of 3 float values from a line assuming the first token
	 * on the line is the identifier
	 * 
	 * @param line The line from which to read the 3 values
	 * @param scale2 
	 * @return The set of 3 floating point values read
	 * @throws IOException Indicates a failure to process the line
	 */
	private Vector3 readVector3(String line) throws IOException {
		StringTokenizer tokens = new StringTokenizer(line, " ");
		
		tokens.nextToken();
		
		try {
			float x = Float.parseFloat(tokens.nextToken());
			float y = Float.parseFloat(tokens.nextToken());
			float z = Float.parseFloat(tokens.nextToken());
			
			return new Vector3(x*scale,y*scale,z*scale);
		} catch (NumberFormatException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/**
	 * Read a set of 2 float values from a line assuming the first token
	 * on the line is the identifier
	 * 
	 * @param line The line from which to read the 3 values
	 * @return The set of 2 floating point values read
	 * @throws IOException Indicates a failure to process the line
	 */
/*	private Tuple2 readTuple2(String line) throws IOException {
		StringTokenizer tokens = new StringTokenizer(line, " ");
		
		tokens.nextToken();
		
		try {
			float x = Float.parseFloat(tokens.nextToken());
			float y = Float.parseFloat(tokens.nextToken());
			
			return new Tuple2(x,y);
		} catch (NumberFormatException e) {
			throw new IOException(e.getMessage());
		}
	}*/
	
	/**
	 * Read a set of face data from the line
	 * 
	 * @param line The line which to interpret as face data
	 * @return The face data extracted from the line
	 * @throws IOException Indicates a failure to process the line
	 */
	private void readFace(String line) throws IOException {
		StringTokenizer points = new StringTokenizer(line, " ");
		
		points.nextToken();
		int faceCount = points.countTokens();
		
		// currently we only support triangels so anything other than
		// 3 verticies is invalid
		if (faceCount != 3) {
			return ;
		} else {
		
		// create a new face data to populate with the values from the line
		//Face face = new Face(faceCount);
		try {
			// for each line we're going to read 3 bits of data, the index
			// of the vertex, the index of the texture coordinate and the
			// normal.
			int f=0;
			//Vector3 face=new Vector3();
			Face face=new Face();
			for (int i=0;i<faceCount;i++) {
				StringTokenizer parts = new StringTokenizer(points.nextToken(), "/");
				
				int v = Integer.parseInt(parts.nextToken());
				//int t = Integer.parseInt(parts.nextToken());
				//int n = Integer.parseInt(parts.nextToken());
		//		System.out.println(""+v+","+t+","+n+",");
				// we have the indicies we can now just add the point
				// data to the face.
			/*	face.addPoint((Vector3) verts.get(v-1),
						      (Tuple2) texCoords.get(t-1),
						      (Vector3) normals.get(n-1));*/

				face.set(f,verts.get(v-1));
				f++;
				
			}
			face.calcNormal();
			indices.add(face);
		} catch (NumberFormatException e) {
			throw new IOException(e.getMessage());
		}
		
		
	//	return face;
		}
	}
	/*public Object3dList getHull() {
		return hull;
	}*/

	public ArrayList<Face> getFaces() {
		return indices;
	}

	public ArrayList<InternalPoint> getPoints() {
		return points;
	}


	
	public int getnPontos() {
		return nPontos;
	}	
}
