<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>
       <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Topup Agent</h3>
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
                    <h2>Topup Agent</small></h2>
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
                  	<br />
                    <table id="topupAgentPayment" class="table table-striped">
                      <tbody>
                      	<tr>
                          <td width="150">Status</td>
                          <td width="150"> : </td>
                          <td align="right">${status}</td>
                        </tr>
                        <tr>
                          <td width="150">Transaction Number</td>
                          <td width="150"> : </td>
                          <td align="right">${transactionNumber}</td>
                        </tr>
                        <tr>
                          <td width="150">From Account</td>
                          <td width="150"> : </td>
                          <td align="right">${member.username}</td>
                        </tr>
                        <tr>
                          <td width="150">To Member</td>
                          <td width="150"> : </td>
                          <td align="right">${toAccount}</td>
                        </tr>
                        <tr>
                          <td width="150">Amount</td>
                          <td width="150"> : </td>
                          <td align="right">${amount}</td>
                        </tr>
                        <tr>
                          <td width="150">Fee</td>
                          <td width="150"> : </td>
                          <td align="right">${fee}</td>
                        </tr>
                        <tr>
                          <td width="150">Total Amount</td>
                          <td width="150"> : </td>
                          <td align="right">${totalAmount}</td>
                        </tr>
                        <tr>
                          <td width="150">Description</td>
                          <td width="150"> : </td>
                          <td align="right">${description}</td>
                        </tr>
                      </tbody>
                    </table>
                    <hr/>
                    
                    <div class="form-group">
          			  	 <input id="username" name="username" value="${member.username}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
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
