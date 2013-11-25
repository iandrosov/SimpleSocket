%ifvar mode equals('edit')%
  %ifvar disableport equals('true')%
    %invoke wm.server.net.listeners:disableListener%
    %endinvoke%
  %endif%
%endif%

%invoke wm.server.net.listeners:getListener%

<HTML>
  <HEAD>
    <meta http-equiv="Pragma" content="no-cache">
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
    <META HTTP-EQUIV="Expires" CONTENT="-1">
    <LINK REL="stylesheet" TYPE="text/css" HREF="/WmRoot/webMethods.css">
    <SCRIPT SRC="/WmRoot/webMethods.js.txt"></SCRIPT>
    <TITLE>Integration Server -- Port Access Management</TITLE>

    <SCRIPT Language="JavaScript">
        function confirmDisable()
        {
          var enabled = "%value ../listening%";

          if(enabled == "primary")
          {
            alert("Port must be disabled to edit these settings.  Primary port cannot be disabled.  To edit these settings, please select a new primary port");
            return false;
          }
          else if(enabled == "true")
          {
            if(confirm("Port must be disabled so that you can edit these settings.  Would you like to disable the port?"))
            {
              document.location.replace("configSOCKET.dsp?listenerKey=%value -code listenerKey%&pkg=%value pkg%%ifvar listenerType%&listenerType=%value -code listenerType%%endif%&mode=edit&disableport=true");
            }
          }
          else
            document.location.replace("configSOCKET.dsp?listenerKey=%value -code listenerKey%&pkg=%value pkg%%ifvar listenerType%&listenerType=%value -code listenerType%%endif%&mode=edit");

          return false;
        }

        function setupData() {
            %ifvar port%
            document.properties.operation.value = "update";
            document.properties.oldPkg.value = "%value pkg%";
            %else%
            document.properties.operation.value = "add";
            %endif%
        }
    </SCRIPT>

  </HEAD>


  <body onLoad="setupData();setNavigation('/WmRoot/security-ports.dsp', 'doc/OnlineHelp/WmRoot.htm#CS_Security_Ports_EditSOCK.htm', 'foo');">
    <TABLE width="100%">
      <TR>
        <TD class="menusection-Security" colspan=2>
          Security &gt;
          Ports &gt;
          %ifvar ../mode equals('view')%
            View Socket Listener Details
          %else%
            Edit Socket Listener Configuration
          %endif%
        </TD>
      </TR>
      <TR>
        <TD colspan="2">
          <UL>
            <LI><A HREF="/WmRoot/security-ports.dsp">Return to Ports</A></LI>
            %ifvar ../mode equals('view')%
              %ifvar ../listening equals('primary')%
              %else%
                <LI><A onclick="return confirmDisable();" HREF="">
                  Edit Socket Listener Configuration
                </A></LI>
              %endif%
            %endif%
          </UL>
        </TD>
      </TR>
      <TR>
        <TD><IMG SRC="/WmRoot/images/blank.gif" height=10 width=10></TD>
        <TD>
          <TABLE class="%ifvar ../mode equals('view')%tableView%else%tableForm%endif%">
        <tr>
            <TD class="heading" colspan="2">Socket Listener Configuration</TD>
        <tr>

        <form onLoad="setupData();" name="properties" action="/WmRoot/security-ports.dsp" method="POST">
        <input type="hidden" name="factoryKey" value="Generic/SOCKET">
        <input type="hidden" name="operation">
        <input type="hidden" name="listenerKey" value="%value listenerKey%">
        <input type="hidden" name="oldPkg">
        %ifvar listenerType%
          <input type="hidden" name="listenerType" value="%value listenerType%">
        %endif%

	

            <td class="oddrow">Port</td>
            <td class="%ifvar ../mode equals('view')%oddrowdata-l%else%oddrow-l%endif%">
              %ifvar ../mode equals('view')%
                %value port%
              %else%
                <input name="port" value="%value port%">
              %endif%
            </td>
        </tr>
        <tr>
            <td class="evenrow">Package Name</td>
            <td class="%ifvar ../mode equals('view')%evenrowdata-l%else%evenrow-l%endif%">
              %ifvar ../mode equals('view')%
                %value pkg%
              %else%
                %invoke wm.server.packages:packageList%
                <select name="pkg">
                %loop packages%
                    %ifvar enabled equals('false')%
                    %else%
                    %ifvar ../pkg -notempty%
                    <option %ifvar ../pkg vequals(name)%selected %endif%value="%value name%">%value name%</option>
                    %else%
                    <option %ifvar name equals('WmRoot')%selected %endif%value="%value name%">%value name%</option>
                    %endif%
                    %endif%
                %endloop%
                </select>
                %endinvoke%
              %endif%
            </td>
        </tr>
	<tr>
                <td class="oddrow">Bind Address (optional)</td>
                <td class="%ifvar ../mode equals('view')%oddrowdata-l%else%oddrow-l%endif%">
				%ifvar ../mode equals('view')%
                  %value bindAddress%
                %else%
                    <input name="bindAddress" value="%value bindAddress%">
                %endif%
              </td>
        </tr>


	<tr>
            <td class="space" colspan="2">&nbsp;</td>
        </tr>
          <tr>
              <td class="heading" colspan="2">Listener Specific Configuration</td>
          </tr>
          <tr>
              <td class="oddrow">Plugin Class</td>
              <td class="%ifvar ../mode equals('view')%oddrowdata-l%else%oddrow-l%endif%">
                <script>writeEdit('%value mode%', 'plugin', '%value -code plugin%');</script>
              </td>
          </tr>

          <tr>
              <td class="oddrow">Socket Timeout value (millisecond)</td>
              <td class="%ifvar ../mode equals('view')%oddrowdata-l%else%oddrow-l%endif%">
                <script>writeEdit('%value mode%', 'so_timeout', '%value -code so_timeout%');</script>
              </td>
          </tr>

	<tr>
            <td class="space" colspan="2">&nbsp;</td>
        </tr>
          <tr>
              <td class="heading" colspan="2">Processing Service</td>
          </tr>
          <tr>
              <td class="oddrow">Service Name</td>
              <td class="%ifvar ../mode equals('view')%oddrowdata-l%else%oddrow-l%endif%">
                <script>writeEdit('%value mode%', 'service_name', '%value -code service_name%');</script>
              </td>
          </tr>


        %ifvar mode equals('view')%
        %else%
          <tr>
              <td colspan="2" class="action">
                  <input type="submit" value="Save Changes">
              </td>
          </tr>
        %endif%
        </form>
      </table>
    </td>
  </tr>
</table>
</body>
</html>
%endinvoke%
