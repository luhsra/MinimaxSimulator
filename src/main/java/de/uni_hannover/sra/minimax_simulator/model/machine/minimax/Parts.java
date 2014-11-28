package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

public class Parts
{
	// Parts
	public static final String	MEMORY						= "MEMORY";
	public static final String	ALU							= "ALU";

	public static final String	ALU_COND_PORT				= "ALU_COND_PORT";

	public static final String	MUX_A						= "MUX_A";
	public static final String	MUX_B						= "MUX_B";
	public static final String	MUX_INPUT					= "MUX_INPUT";

	public static final String	REGISTER_					= "REGISTER_";

	public static final String	MAR							= "MAR";
	public static final String	MDR							= "MDR";
	public static final String	IR							= "IR";
	public static final String	PC							= "PC";
	public static final String	ACCU						= "ACCU";

	public static final String	MDR_SELECT					= "MDR_SELECT";

	public static final String	SIGN_EXTENSION				= "SIGN_EXTENSION";

	// Groups
	public static final String	GROUP_BASE_REGISTERS		= "GROUP_BASE_REGISTERS";
	public static final String	GROUP_EXTENDED_REGISTERS	= "GROUP_EXTENDED_REGISTERS";
	public static final String	GROUP_ALL_REGISTERS			= "GROUP_ALL_REGISTERS";
	public static final String	GROUP_MUX_CONSTANTS			= "GROUP_MUX_CONSTANTS";
	public static final String	GROUP_MUX_LABEL				= "GROUP_MUX_LABEL";
	public static final String	GROUP_MUX_LINE				= "GROUP_MUX_LINE";
	public static final String	GROUP_MUX_EXT_REGISTERS		= "GROUP_MUX_EXT_REGISTERS";
	public static final String	GROUP_MUX_BASE_REGISTERS	= "GROUP_MUX_BASE_REGISTERS";

	// Virtuals
	public static final String	MUX_SPACING					= "MUX_SPACING";
	public static final String	ALU_LINE					= "ALU_LINE";
	public static final String	MUX_LINE					= "MUX_LINE";

	// Pins
	public static final String	MUX_A_OUT					= "MUX_A_OUT";
	public static final String	MUX_A_SELECT				= "MUX_A_SELECT";
	public static final String	MUX_B_OUT					= "MUX_B_OUT";
	public static final String	MUX_B_SELECT				= "MUX_B_SELECT";

	public static final String	MDR_SELECT_OUT				= "MDR_SELECT_OUT";
	public static final String	MDR_SELECT_IN_0				= "MDR_SELECT_IN_0";
	public static final String	MDR_SELECT_IN_1				= "MDR_SELECT_IN_1";
	public static final String	MDR_SELECT_SELECT			= "MDR_SELECT_SELECT";

	public static final String	ALU_DATA_OUT				= "ALU_DATA_OUT";
	public static final String	ALU_COND_OUT				= "ALU_COND_OUT";
	public static final String	ALU_A_IN					= "ALU_A_IN";
	public static final String	ALU_B_IN					= "ALU_B_IN";
	public static final String	ALU_CTRL_IN					= "ALU_CTRL_IN";

	public static final String	MEMORY_CS					= "MEMORY_CS";
	public static final String	MEMORY_RW					= "MEMORY_RW";
	public static final String	MEMORY_ADR					= "MEMORY_ADR";
	public static final String	MEMORY_DI					= "MEMORY_DI";
	public static final String	MEMORY_DO					= "MEMORY_DO";

	// Suffixes
	public static final String	_JUNCTION					= "_JUNCTION";
	public static final String	_OUT_JUNCTION				= "_OUT_JUNCTION";
	public static final String	_PORT						= "_PORT";
	public static final String	_LABEL						= "_LABEL";
	public static final String	_CU							= "_CU";
	public static final String	_ANCHOR						= "_ANCHOR";
	public static final String	_CONSTANT					= "_CONSTANT";
	public static final String	_REGISTER					= "_REGISTER";

	// Wires
	public static final String	_WIRE						= "_WIRE";
	public static final String	_WIRE_PORT					= "_WIRE_PORT";
	public static final String	_WIRE_DATA_IN				= "_WIRE_DATA_IN";
	public static final String	_WIRE_DATA_OUT				= "_WIRE_DATA_OUT";
	public static final String	_WIRE_ENABLED				= "_WIRE_ENABLED";
	public static final String	_WIRE_CTRL					= "_WIRE_CTRL";
	public static final String	_WIRE_SELECT				= "_WIRE_SELECT";
}