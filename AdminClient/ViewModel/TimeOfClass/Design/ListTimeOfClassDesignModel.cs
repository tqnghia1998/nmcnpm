using System.Collections.ObjectModel;

namespace AdminClient
{
    /// <summary>
    /// View model cho <see cref="ListTimeOfClassControl"/>
    /// </summary>
    public class ListTimeOfClassDesignModel: ListTimeOfClassViewModel
    {
        #region Singleton

        public static ListTimeOfClassDesignModel Instance => new ListTimeOfClassDesignModel();

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public ListTimeOfClassDesignModel()
        {
            Items = new ObservableCollection<TimeOfClassItemViewModel>()
            {
                new TimeOfClassItemViewModel()
                {
                    DayInTheWeek = "Thứ 2",
                },
                
            };
        }

        #endregion
    }
}
