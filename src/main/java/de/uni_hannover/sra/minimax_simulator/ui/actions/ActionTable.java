//package de.uni_hannover.sra.minimax_simulator.ui.actions;
//
//import java.lang.reflect.Constructor;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.swing.Action;
//
//public class ActionTable
//{
//	private final static ActionTable _instance = new ActionTable();
//
//	public final static ActionTable getInstance()
//	{
//		return _instance;
//	}
//
//	private Map<Class<? extends Action>, Action> _actions;
//
//	private ActionTable()
//	{
//		_actions = new HashMap<Class<? extends Action>, Action>();
//	}
//
//	public Action getGlobalAction(Class<? extends Action> clazz)
//	{
//		Action a = _actions.get(clazz);
//		if (a == null)
//		{
//			try
//			{
//				Constructor<? extends Action> cons = clazz.getConstructor();
//				a = cons.newInstance();
//				_actions.put(clazz, a);
//			}
//			catch (Exception e)
//			{
//				// Some serious error, terminate program
//				throw new Error("Cannot instantiate action: " + clazz.getName(), e);
//			}
//		}
//		return a;
//	}
//}