/**
 * This class mainly provides the method to generate dynamic query. And it supports
 * the following conditions.
 * {columnName}.startsWith=value                     where {columnName} like '{value}%'
 * {columnName}.endsWith=value                       where {columnName} like '%{value}'
 * {columnName}.contains=value                       where {columnName} like '%{value}%'
 * {columnName}.containsIc=value                     where {columnName} like '%{value}%' (ignoreCase)
 * {columnName}.eq=value                             where {columnName} [like| =] 'value'
 * {columnName}.like=value                           where {columnName} [like| =] 'value'
 * {columnName}.neq=value                            where {columnName} != 'value'
 * {columnName}.between=min,max                      where {columnName} between 'min' and 'max'
 * {columnName}.gt=value                             where {columnName} > value
 * {columnName}.gte=value                            where {columnName} >= value
 * {columnName}.lt=value                             where {columnName} < value
 * {columnName}.lte=value                            where {columnName} <= value
 * {columnName}.in=value1,value2....value-n          where {columnName} in (values)
 * {columnName}.nin=value1,value2....value-n         where {columnName} not in (values)
 * {columnName}.sort=a or d                          Sort by {columnName} d ? desc : asc
 * pageable.props=pageNum,count                      creates the pageable by taking pageNum and count.
 *
 * @Example :   if user makes an request as "host:port/employees?name.startsWith=V&salary.gte=30000&domain.in=Testing,Development"
 *            then it will create the following query.
 *
 *            "Select * from employee e where e.name like 'V%' and salary >= 30000 and domain in ('Testing', 'Development')"
 *            To Understand easily I have converted it to a SQL query. Mongo query looks as follows.
 *            "{ name: {$regex:'V^'}, salary: {$gte: 30000}, domain: {$in: ['Development', 'Testing']}}"
 */