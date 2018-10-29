using System.Collections.ObjectModel;

namespace AdminClient
{
    /// <summary>
    /// View model cho <see cref="TimeOfClassItemControl"/>
    /// </summary>
    public class TimeOfClassItemDesignModel: TimeOfClassItemViewModel
    {
        #region Singleton

        public TimeOfClassItemDesignModel Instance => new TimeOfClassItemDesignModel();

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public TimeOfClassItemDesignModel()
        {
            DayInTheWeek = "Thứ 2";
        }

        #endregion
    }
}
