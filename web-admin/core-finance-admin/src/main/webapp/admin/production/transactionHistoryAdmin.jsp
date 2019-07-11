<%@include file="header.jsp" %>
        
       <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Transaction History</h3>
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
              <div class="col-md-6 col-sm-6 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Member Details</small></h2>
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
                    <table class="table table-striped">
                      <tbody>
                        <tr>
                          <td width="150">Group</td>
                          <td width="150"> : </td>
                          <td align="right">${groupName}</td>
                        </tr>
                        <tr>
                          <td width="150">Username</td>
                          <td width="150"> : </td>
                          <td align="right">${username}</td>
                        </tr>
                        <tr>
                          <td width="150">Name</td>
                          <td width="150"> : </td>
                          <td align="right">${name}</td>
                        </tr>
                        <tr>
                          <td width="150">Email</td>
                          <td width="150"> : </td>
                          <td align="right">${email}</td>
                        </tr>
                      </tbody>
                    </table>
                    <hr/>
                  </div>
                </div>
              </div>


              <div class="col-md-6 col-sm-6 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Account Details <small>${accountName}</small></h2>
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

                     <table class="table table-striped">
                      <tbody>
                        <tr>
                          <td width="150">Account Balance</td>
                          <td width="150"> : </td>
                          <td align="right">${balance}</td>
                        </tr>
                        <tr>
                          <td width="150">Reserved Amount</td>
                          <td width="150"> : </td>
                          <td align="right">${reservedAmount}</td>
                        </tr>
                        <tr>
                          <td width="150">Credit Limit</td>
                          <td width="150"> : </td>
                          <td align="right">${creditLimit}</td>
                        </tr>
                        <tr>
                          <td width="150">Monthly Limit</td>
                          <td width="150"> : </td>
                          <td align="right">${upperCreditLimit}</td>
                        </tr>
                      </tbody>
                    </table>
                   <hr/>
                  </div>
                </div>
              </div>

              
               <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Transactions</h2>
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
                     <div id="reportrange" class="pull-right" style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc">
                      <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
                      <span></span> <b class="caret"></b>
                    </div><div class="pull-right"><button class="btn btn-default" type="button" onclick="reloadTable()">Go!</button></div>
              	 </div>
               
                  <div class="x_content">
                    <table id="trxtable" class="table table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
                      <thead>
                        <tr>
                          <th>Transaction Date</th>
                          <th>From / To</th>
                          <th>Amount</th>
                          <th>Remark</th>
                          <th>Transaction Type</th>
                          <th>Description</th>
                          <th>Trace Number</th>
                          <th>Transaction Number</th>
                          <th>Action</th>
                        </tr>
                      </thead>                      
                    </table>
                    <input  type="hidden" name="from" id="from" value="${fromDate}">
					<input  type="hidden" name="to" id="to" value="${toDate}">
					
                  </div>
                </div>
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
	
	<script type="text/javascript">
               $(document).ready(function() {
                  $('#reportrange').daterangepicker(
                     {
                        startDate: moment(),
                        endDate: moment(),
                        minDate: '01/01/2017',
                        maxDate: '12/31/2030',
                        dateLimit: { days: 60 },
                        showDropdowns: true,
                        showWeekNumbers: true,
                        timePicker: false,
                        timePickerIncrement: 1,
                        timePicker12Hour: true,
                        ranges: {
                           'Today': [moment(), moment().add('days', 1)],
                           'Yesterday': [moment().subtract('days', 1), moment()],
                           'Last 7 Days': [moment().subtract('days', 6), moment().add('days', 1)],
                           'Last 30 Days': [moment().subtract('days', 29), moment().add('days', 1)],
                           'This Month': [moment().startOf('month'), moment().endOf('month')]
                        },
                        opens: 'left',
                        buttonClasses: ['btn btn-default'],
                        applyClass: 'btn-small btn-primary',
                        cancelClass: 'btn-small',
                        format: 'MM/DD/YYYY',
                        separator: ' to ',
                     },
                     function(start, end) {
                      $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
     				  $('#from').val(start.format('YYYY-MM-DD'));
    				  $('#to').val(end.format('YYYY-MM-DD'));
                     }
                  );
                  //Set the initial state of the picker label
                  $('#reportrange span').html(moment().format('MMMM D, YYYY') + ' - ' + moment().format('MMMM D, YYYY'));
               });
               </script>
	
	
 <script>
 		$("#trxtable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'Blfrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/transactionHistoryData",
       					 "data" : function ( d ) {
 		               	  	d.username = "${username}";
 		            	  	d.accountID = "${accountID}";
 		            	  	d.fromDate = document.getElementById("from").value;
 		            	  	d.toDate = document.getElementById("to").value;
 			           		}
 			          },
       				 "columns" : [ {
								"data" : "transactionDate"
							}, {
								"data" : "toFromMember"
							}, {
								"data" : "amount"
							}, {
								"data" : "remark"
							}, {
								"data" : "transferType"
							}, {
								"data" : "description"
							}, {
								"data" : "traceNumber", className: 'none'
							}, {
								"data" : "transactionNumber", className: 'none'
							}, {
								"data" : null, className: 'none',
								"render" : function ( data, type, row ) {
                   					 return "<a href='reverseInquiry?transactionNumber=" + data.transactionNumber + "&description=" + data.description + "&remark=" + data.remark + "&transactionDate=" + data.transactionDate + "&amount=" + data.amount + "&toFromMember=" + data.toFromMember + "&traceNumber=" + data.traceNumber+"' class='btn btn-danger btn-xs'><i class='fa fa-trash-o'></i> Reverse</a>";
               					}
							}]
					});
	</script>
	
	<c:if test="${not empty fn:trim(notification)}">
		<script type="text/javascript">
			$(function(){
		 	 new PNotify({
		        title: '${title}',
		        text: '${message}',
		        type: '${notification}',
		        styling: 'bootstrap3'
		        });
			});
		</script>
	</c:if>
	</body>
</html>
