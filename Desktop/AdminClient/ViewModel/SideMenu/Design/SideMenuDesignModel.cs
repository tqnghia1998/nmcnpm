using System.Collections.ObjectModel;

namespace AdminClient
{
    /// <summary>
    /// View model at design time for <see cref="SideMenuControl"/>
    /// </summary>
    public class SideMenuDesignModel: SideMenuViewModel
    {
        #region Singleton

        public static SideMenuDesignModel Instance => new SideMenuDesignModel();

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public SideMenuDesignModel()
        {
        }

        #endregion
    }
}
