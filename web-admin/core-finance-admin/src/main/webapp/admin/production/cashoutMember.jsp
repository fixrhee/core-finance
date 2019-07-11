<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
                    <h2>Cashout Member</h2>
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
                    <form id="cashoutmemberform" data-parsley-validate action="/admin/cashoutMemberInquiry" method="POST" modelAttribute="cashoutmember" class="form-horizontal form-label-left">

                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="trffromAccount">From Member :</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" name="fromAccount" id="fromAccount" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="trfAmount">Amount :</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <select id="amount" name="amount" required="required" class="form-control col-md-7 col-xs-12" tabindex="-1">
                            <option>Select Amount</option>
                            <option name="amount0" id="amount0" value="20000">Rp 20000</option>
							<option name="amount1" id="amount1" value="50000">Rp 50000</option>
							<option name="amount2" id="amount2" value="100000">Rp 100000</option>
							<option name="amount3" id="amount3" value="150000">Rp 150000</option>
							<option name="amount4" id="amount4" value="200000">Rp 200000</option>
							<option name="amount5" id="amount5" value="250000">Rp 250000</option>
							<option name="amount6" id="amount6" value="300000">Rp 300000</option>
							<option name="amount7" id="amount7" value="350000">Rp 350000</option>
							<option name="amount8" id="amount8" value="400000">Rp 400000</option>
							<option name="amount9" id="amount9" value="450000">Rp 450000</option>
							<option name="amount10" id="amount10" value="500000">Rp 500000</option>
						  </select>
                        </div>
                      </div>
                      
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="trfAmount">OTP :</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="password" id="otp" name="otp" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="trfDescription">Description :</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="description" name="description" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
          			 
          			  <div class="form-group">
          			  	  <input name="toAccount" id="toAccount" value={member.username}  class="form-control col-md-7 col-xs-12" type="hidden">
          			  	  <input id="username" name="username" value="${member.username}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
                      </div>
                     
                      <div class="ln_solid"></div>
                      <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
           				  <button class="btn btn-primary" type="reset">Reset</button>
                          <button type="submit" id="sbm" class="btn btn-success">Submit</button>
                        </div>
                      </div>

                    </form>
                  </div>
                </div>
              </div>
            </div>

              </div>
            </div>
          </div>
        </div>
        <!-- /page content -->

<%@include file="footer.jsp" %>

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