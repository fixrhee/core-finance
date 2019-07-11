<%@include file="header.jsp" %>
        
       <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Bank Account Transfer List</h3>
              </div>
              
               <div class="title_right">
                <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
                 <div class="input-group">
                    <br /> <br /> </div>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-24">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Bank Account Details</small></h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                      </li>
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                        <ul class="dropdown-menu" role="menu">
                          <li><a href="#">Settings 1</a>
                          </li>
                        </ul>
                      </li>
                      <li><a class="close-link"><i class="fa fa-close"></i></a>
                      </li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                  
                  <div class="x_content">
                    <table id="trxdetailbankaccount" class="table table-striped">
                      <tbody>
                        <tr>
                          <td width="150">Bank Code</td>
                          <td width="150"> : </td>
                          <td align="right">${bankCode}</td>
                        </tr>
                        <tr>
                          <td width="150">Bank Name</td>
                          <td width="150"> : </td>
                          <td align="right">${bankName}</td>
                        </tr>
                        <tr>
                          <td width="150">Account Name</td>
                          <td width="150"> : </td>
                          <td align="right">${accountName}</td>
                        </tr>
                        <tr>
                          <td width="150">Account Number</td>
                          <td width="150"> : </td>
                          <td align="right">${accountNumber}</td>
                        </tr>
                        <tr>
                          <td width="150">Description</td>
                          <td width="150"> : </td>
                          <td align="right">${description}</td>
                        </tr>
                        <tr>
                          <td width="150">Created Date</td>
                          <td width="150"> : </td>
                          <td align="right">${createdDate}</td>
                        </tr>
                      </tbody>
                    </table>
                    <hr/>
                  </div>
                </div>
              </div>

      		<div class="form-group">
                <input id="username" name="username" value="${member.username}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
                <!--input id="sessionID" name="sessionID" value="${sessionID}" class="date-picker form-control col-md-7 col-xs-12" type="hidden"-->
            </div>
      		
            </div>
          </div>
        </div>
        <!-- /page content -->
   
<%@include file="footer.jsp" %>

<script>
	function reloadTable(){
		var table = $('#trxtable').DataTable();
		table.ajax.reload( null, false );
	}
</script>
	
<script>
 		$("#trxdetailbankaccount")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'Blfrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/detailBankAccountData",
       					 "data" : function ( d ) {
 		               	  	
 			           		}
 			          }
					});
	</script>
	</body>
</html>
