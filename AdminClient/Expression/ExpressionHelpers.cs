using System;
using System.Linq.Expressions;
using System.Reflection;

namespace AdminClient
{
    /// <summary>
    /// Bổ trợ cho <see cref="Expression"/>
    /// </summary>
    public static class ExpressionHelpers
    {
        /// <summary>
        /// Biên dịch một expression và lấy giá trị hàm trả về
        /// </summary>
        /// <typeparam name="T"> Kiểu dữ liệu hàm trả về </typeparam>
        /// <param name="lambda">expression để biên dịch </param>
        /// <returns></returns>
        public static T GetPropertyValue<T>(this Expression<Func<T>> lambda)
        {
            return lambda.Compile().Invoke();
        }

        /// <summary>
        /// Set giá trị của property của expression mà chứa property
        /// </summary>
        /// <typeparam name="T"> Kiểu dự liệu để set </typeparam>
        /// <param name="lambda"> expression </param>
        /// <param name="value"> value để gán cho property </param>
        public static void SetPropertyValue<T>(this Expression<Func<T>> lambda, T value)
        {
            // Chuyển một lambda () => some.Property thành some.Property
            var expression = (lambda as LambdaExpression).Body as MemberExpression;

            // Lấy thông tin của property để chúng ta có thể set nó
            var propertyInfo = (PropertyInfo)expression.Member;
            var target = Expression.Lambda(expression.Expression).Compile().DynamicInvoke();

            // Set giá trị cho property
            propertyInfo.SetValue(target, value);
        }
    }
}
