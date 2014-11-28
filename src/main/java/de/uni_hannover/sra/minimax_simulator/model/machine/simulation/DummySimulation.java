//package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;
//
//import java.util.Random;
//
//import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
//import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MapMemory;
//
//public class DummySimulation extends AbstractSimulation
//{
//	private final TrackableInteger	_alu	= new TrackableInteger();
//
//	@Override
//	public Trackable<Integer> getAluResult()
//	{
//		return _alu;
//	}
//
//	private final TrackableInteger	_register	= new TrackableInteger();
//
//	@Override
//	public Trackable<Integer> getRegisterValue(String name)
//	{
//		return _register;
//	}
//
//	private MachineMemory	_mem	= new MapMemory(8);
//
//	@Override
//	public MachineMemory getMemoryState()
//	{
//		return _mem;
//	}
//
//	@Override
//	protected void resetImpl()
//	{
//		resetCycles();
//	}
//
//	@Override
//	protected void initImpl()
//	{
//		resetCycles();
//		_alu.set(42);
//		_register.set(15);
//	}
//
//	@Override
//	protected void stopImpl()
//	{
//		resetCycles();
//		_alu.set(null);
//		_register.set(null);
//	}
//
//	@Override
//	protected void stepImpl()
//	{
//		randomize();
//		incrementCycles();
//	}
//
//	@Override
//	protected void runImpl()
//	{
//		for (int i = 0; i < 100 && !paused(); i++)
//		{
//			incrementCycles();
//
//			try
//			{
//				Thread.sleep(100);
//			}
//			catch (InterruptedException e)
//			{
//			}
//		}
//		randomize();
//	}
//
//	private Random	_random	= new Random();
//
//	private void randomize()
//	{
//		_alu.set(_random.nextInt(Integer.MAX_VALUE));
//		_register.set(_random.nextInt(Integer.MAX_VALUE));
//	}
//
//	@Override
//	public int getCurrentSignalRow()
//	{
//		return 0;
//	}
//}