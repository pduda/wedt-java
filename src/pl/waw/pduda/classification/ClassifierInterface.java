package pl.waw.pduda.classification;


public interface ClassifierInterface 
{
	void init(String [] classes);
	void reset();
	void train(String className,String [] attrs);
	String classify(String [] data);
}
