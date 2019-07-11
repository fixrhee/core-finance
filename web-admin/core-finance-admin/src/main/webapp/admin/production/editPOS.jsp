<%@include file="header.jsp" %>

        <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Payment Collection</h3>
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
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Edit Point of Sales</h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                      </li>
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                        <ul class="dropdown-menu" role="menu">
                          <li><a href="#">Settings</a>
                          </li>
                        </ul>
                      </li>
                      <li><a class="close-link"><i class="fa fa-close"></i></a>
                      </li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                 
                   <div class="x_content">
        			 <br />
                    <form id="editposform" data-parsley-validate action="/admin/pos" method="POST" 
                    	modelAttribute="editpos" class="form-horizontal form-label-left">
					  		<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="posUsername">
									Username
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="username" name="username" value="${member.username}" readonly
										class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="posName">
									Name
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="name" name="name" value="${name}"
										class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="posPIC">
									PIC
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="pic" name="pic" value="${pic}"
										class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="posMsisdn">
									MSISDN
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="msisdn" name="msisdn" value="${msisdn}" 
										 class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="posEmail">
									Email
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="email" name="email" value="${email}"
										class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="posAddress">
									Address
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="address" name="address" value="${address}"
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="posCity">
									City
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="city" name="city" value="${city}"
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="posPostalCode">
									Postal Code
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="postalCode" name="postalCode" value="${postalCode}"
										required="required" class="form-control col-md-7 col-xs-12">
								</div>
							</div>
							<c:choose>
							    <c:when test="${openPayment == 'true'}">
									<div class="form-group">
				                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="posPayment"> Payment :
				                        </label>
				                        <div class="col-md-6 col-sm-6 col-xs-12">
				                          <input type="radio" id="openPayment" name="payment" value="openPayment" checked="checked">Open Payment<br>
				                          <input type="radio" id="fixedAmount" name="payment" value="fixedAmount" onclick="javascript:yesnoCheck();">Fixed Amount
				                        </div>
				                     </div>
				                     <div id="ifYes" style="display:none" class="form-group">
				                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="posAmount">Amount :
				                        </label>
				                        <div class="col-md-6 col-sm-6 col-xs-12">
				                          <input type="text" id="amount" name="amount"  class="form-control col-md-7 col-xs-12">
				                        </div>
				                     </div>
								</c:when>
								<c:otherwise>
									<div class="form-group">
				                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="posPayment"> Payment :
				                        </label>
				                        <div class="col-md-6 col-sm-6 col-xs-12">
				                          <input type="radio" id="openPayment" name="payment" value="openPayment" onclick="javascript:yesnoCheck();">Open Payment<br>
				                          <input type="radio" id="fixedAmount" name="payment" value="fixedAmount" checked="checked" onclick="javascript:yesnoCheck();">Fixed Amount
				                        </div>
				                     </div>
				                     <div id="ifYes" style="display:block" class="form-group">
				                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="posAmount">Amount :
				                        </label>
				                        <div class="col-md-6 col-sm-6 col-xs-12">
				                          <input type="text" id="amount" name="amount"  class="form-control col-md-7 col-xs-12">
				                        </div>
				                     </div>
		                     </c:otherwise>
							</c:choose>
														
							<div class="form-group">
							  <input id="id" name="id" value="${id}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
	                          <input id="username" name="username" value="${member.username}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
	                       </div>
	                      	
		                  <div class="ln_solid"></div>
	                      <div class="form-group">
	                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
	                  		  <button class="btn btn-primary" type="button" onclick="history.back()">Back</button>
	                          <button name="confirm" id="confirmedit" type="submit" value="edit" class="btn btn-success">Submit</button>
	                        </div>
	                      </div>
                    </form>
               </div>
              </div>
            </div>
          </div>
        </div>
	</div>
    <!-- /page content -->

	<%@include file="footer.jsp" %>
	<script type="text/javascript">
		function yesnoCheck() {
		    if (document.getElementById('fixedAmount').checked) {
		        document.getElementById('ifYes').style.display = 'block';
		    }
		    else document.getElementById('ifYes').style.display = 'none';
		}
	</script>
	</body>
</html>