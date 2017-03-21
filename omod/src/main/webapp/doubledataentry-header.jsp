<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><spring:message code="admin.title.short"/></a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("configuration") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/doubledataentry/configuration/manage.form">
			<spring:message code="doubledataentry.manage.configurations"/>
		</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("discrepancy/reports") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/doubledataentry/discrepancy.form">
			<spring:message code="not.known"/>
		</a>
	</li>
</ul>