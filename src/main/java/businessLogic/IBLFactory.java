package businessLogic;

public interface IBLFactory {
	public BLFacade createBL(boolean isLocal) throws Exception;
}
