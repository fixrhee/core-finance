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
						<input type="text" class="form-control" placeholder="Search for...">
							<span class="input-group-btn">
								<button class="btn btn-default" type="button">Go!</button>
							</span>
					</div>
				</div>
			</div>
		</div>

		<div class="clearfix"></div>

		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="x_title">
						<h2>
							Point of Sales Details
						</small>
						</h2>
						<ul class="nav navbar-right panel_toolbox">
							<li>
								<a class="collapse-link">
									<i class="fa fa-chevron-up"></i>
								</a>
							</li>
							<li class="dropdown">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown"
									role="button" aria-expanded="false">
									<i class="fa fa-wrench"></i>
								</a>
								<ul class="dropdown-menu" role="menu">
									<li>
										<a href="#">Settings 1</a>
									</li>
								</ul>
							</li>
							<li>
								<a class="close-link">
									<i class="fa fa-close"></i>
								</a>
							</li>
						</ul>
						<div class="clearfix"></div>
					</div>

					<div class="x_content">
						<table id="trxdetailMember" class="table table-striped">
							<tbody>
								<tr>
									<td width="150">ID </td>
									<td width="150"> : </td>
									<td align="right">${id}</td>
								</tr>
								<tr>
									<td width="150">Username</td>
									<td width="150"> : </td>
									<td align="right">${member.username}</td>
								</tr>
								<tr>
									<td width="150">Name</td>
									<td width="150"> : </td>
									<td align="right">${name}</td>
								</tr>
								<tr>
									<td width="150">PIC</td>
									<td width="150"> : </td>
									<td align="right">${pic}</td>
								</tr>
								<tr>
									<td width="150">Phone No</td>
									<td width="150"> : </td>
									<td align="right">${msisdn}</td>
								</tr>
								<tr>
									<td width="150">Email</td>
									<td width="150"> : </td>
									<td align="right">${email}</td>
								</tr>
								<tr>
									<td width="150">City</td>
									<td width="150"> : </td>
									<td align="right">${city}</td>
								</tr>
								<tr>
									<td width="150">Address</td>
									<td width="150"> : </td>
									<td align="right">${address}</td>
								</tr>
								<tr>
									<td width="150">Postal Code</td>
									<td width="150"> : </td>
									<td align="right">${postalCode}</td>
								</tr>
								<tr>
									<td width="150">Fixed Amount</td>
									<td width="150"> : </td>
									<td align="right">${fixedAmount}</td>
								</tr>
								<c:choose>
							    	<c:when test="${openPayment == 'true'}">
										
									</c:when>
									<c:otherwise>
										<tr>
											<td width="150">Amount</td>
											<td width="150"> : </td>
											<td align="right">${amount}</td>
										</tr>
									</c:otherwise>
								</c:choose>
								<tr>
									<td width="150">Open Payment</td>
									<td width="150"> : </td>
									<td align="right">${openPayment}</td>
								</tr>
							</tbody>
						</table>
						<hr />
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
