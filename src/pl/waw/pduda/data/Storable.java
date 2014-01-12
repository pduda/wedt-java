package pl.waw.pduda.data;
public interface Storable{
	
	public void getXmlRepresentation(XMLWriter w,String path,int i) throws StoreException;
	
	
}
