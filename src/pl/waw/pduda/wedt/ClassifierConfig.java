package pl.waw.pduda.wedt;

public class ClassifierConfig 
{
	private boolean useStems;
	private boolean useHtml;
	private String modeName;
	
	public boolean isUseStems() {
		return useStems;
	}

	public boolean isUseHtml() {
		return useHtml;
	}

	public String getModeName() {
		return modeName;
	}

	public ClassifierConfig(boolean useStems, boolean useHtml, String modeName) 
	{
		this.useStems = useStems;
		this.useHtml = useHtml;
		this.modeName = modeName;
	}

	@Override
	public String toString() 
	{
		return "ClassifierConfig [useStems=" + useStems + ", useHtml="
				+ useHtml + ", modeName=" + modeName + "]";
	}
}
