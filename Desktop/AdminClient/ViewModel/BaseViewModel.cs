using System;
using System.ComponentModel;
using System.Linq.Expressions;
using System.Threading.Tasks;

namespace AdminClient
{
    /// <summary>
    /// A base view model that fires property changed event as needed
    /// </summary>
    public class BaseViewModel : INotifyPropertyChanged
    {
        /// <summary>
        /// The event that is fired when any child property changes its value 
        /// </summary>
        public event PropertyChangedEventHandler PropertyChanged = (sender, e) => { };

        /// <summary>
        /// Call this to fire <see cref="PropertyChanged"/> event
        /// </summary>
        /// <param name="name"></param>
        public void OnPropertyChanged(string name)
        {
            PropertyChanged(this, new PropertyChangedEventArgs(name));
        }

        #region Command Helpers

        /// <summary>
        /// Chạy một lệnh nếu updating flag chưa được set.
        /// Nếu flag là true (biểu thị cho fuction đang chạy) thì action sẽ không được chạy.
        /// Nếu flag là false (function đang không chạy) thì action sẽ được chạy.
        /// Action kết thúc nếu nó chạy và sau đó flag được reset về false
        /// </summary>
        /// <param name="updatingFlag">Cờ hiệu xác định lệnh có đạng chạy hay không</param>
        /// <param name="action"> Action để chạy nếu lệnh đang không chạy </param>
        /// <returns></returns>
        protected async Task RunCommand(Expression<Func<bool>> updatingFlag, Func<Task> action)
        {
            // Kiểm tra nếu cờ là true (có nghĩa là function đang chạy)
            if (updatingFlag.GetPropertyValue())
            {
                return;
            }

            // Set flag thành true biểu thị cho hàm đang được chạy
            updatingFlag.SetPropertyValue(true);

            try
            {
                // run action truyền vào
                await action();
            }
            finally
            {
                // Set flag trở lại thành false ngay lập tức khi nó kết thúc
                updatingFlag.SetPropertyValue(false);
            }
        }


        #endregion
    }
}
