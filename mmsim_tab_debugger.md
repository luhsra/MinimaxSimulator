---
title: Debugger
last_updated: Mar 16, 2016
keywords: debugger, simulation
---

The tab _debugger_ is used for the simulation of the Minimax machine and the microcode.

<p align="center">
  <img src="images/tab_debugger.png" />
</p>

Therefore the memory table is also placed here. Below this there is a list of registers showing the currently stored value of the register which is editable by double clicking it. The current result of the ALU is display too.
On the right side below the ALU result a simplified version of the signal table is shown. Only toggling the breakpoint setting is possible from within the debugger. Pressing <img class="inline" src="images/btn_sim_start.png" /> starts the simulation mode.

<div style="margin-left: 50px">
  <table>
    <tr>
      <td align="center">Button</td>
      <td>Action</td>
    </tr>
    <tr>
      <td align="center"><img src="images/btn_sim_step.png" /></td>
      <td><div style="height:100%; vertical-align:middle; margin-top:15px">simulates one step of the machine</div></td>
    </tr>
    <tr>
      <td align="center"><img src="images/btn_sim_run.png" /></td>
      <td><div style="height:100%; vertical-align:middle; margin-top:15px">runs the microcode up to the next breakpoint or until the program finished</div></td>  
    </tr>
    <tr>
      <td align="center"><img src="images/btn_sim_reset.png" /></td>
      <td><div style="height:100%; vertical-align:middle; margin-top:15px">resets the simulation to the starting state</div></td>  
    </tr>
    <tr>
      <td align="center"><img src="images/btn_sim_stop.png" /></td>
      <td><div style="height:100%; vertical-align:middle; margin-top:15px">quits the simulation mode</div></td>  
    </tr>
  </table>
</div>