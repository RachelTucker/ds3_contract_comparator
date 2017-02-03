<html>
<head>
<style>
table {
  width: 50em;
}
table, th, td {
  border: 1px solid black;
  border-collapse: collapse;
}
</style>
</head>
<body>

<h1>Contract Comparison</h1>
<h2>${oldContract} VS ${newContract}</h2>

<h3>Index</h3>
<table>
<#list indexSections as indexSection>
<tr><th>${indexSection.title}</th><tr>
<#list indexSection.entries as entry>
<tr>
  <td><a href="#${entry.anchor}">${entry.entry}</a></td>
</tr>
</#list>
</#list>
</table>

<#list sections as section>
<h2>${section.title}</h2>
<#list section.tables as table>
<h3 id="${table.anchor}">${table.title}</h3>
<table>
<tr>
  <th>Element</th>
  <th>${oldContract}</th>
  <th>${newContract}</th>
</tr>
<#list table.rows as row>
<tr>
  <td style="padding-left:${row.indent}em;">${row.label}</td>
  <td style="padding-left:${row.indent}em;${row.oldColor}">${row.oldVal}</td>
  <td style="padding-left:${row.indent}em;${row.newColor}">${row.newVal}</td>
</tr>
</#list>
</table>
</#list>
</#list>

</body>
</html>