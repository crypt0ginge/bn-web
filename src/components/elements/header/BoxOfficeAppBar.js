import React from "react";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";
import { withStyles } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import IconButton from "@material-ui/core/IconButton";
import Hidden from "@material-ui/core/Hidden";
import MenuIcon from "@material-ui/icons/Menu";

import { toolBarHeight } from "../../../config/theme";
import RightUserMenu from "./RightUserMenu";
import BoxOfficeLink from "./BoxOfficeLink";
import BoxOfficeEventSelection from "./BoxOfficeEventSelection";
import servedImage from "../../../helpers/imagePathHelper";

const styles = theme => {
	return {
		root: {},
		toolBar: {
			paddingRight: theme.spacing.unit * 2,
			paddingLeft: theme.spacing.unit * 2,
			display: "flex",
			justifyContent: "space-between",
			...toolBarHeight
		},
		logoContainer: {
			display: "flex",
			alignItems: "center"
		},
		headerImage: {
			maxWidth: 45,
			width: "auto"
		},
		rightMenuOptions: {
			alignItems: "center",
			display: "flex"
		},
		verticalDivider: {
			borderLeft: "1px solid #DEE2E8",
			height: 50,
			marginLeft: theme.spacing.unit * 2
		}
	};
};

const BoxOfficeAppBar = props => {
	const { classes, handleDrawerToggle, history, homeLink } = props;

	return (
		<div>
			<AppBar position={"static"} className={classes.root}>
				<Toolbar className={classes.toolBar}>
					{handleDrawerToggle ? (
						<Hidden mdUp implementation="css">
							<IconButton
								color="inherit"
								aria-label="open drawer"
								onClick={handleDrawerToggle}
								className={classes.navIconHide}
							>
								<MenuIcon color="action"/>
							</IconButton>
						</Hidden>
					) : null}
					<div className={classes.logoContainer}>
						<Link to={homeLink}>
							<img
								alt="Header logo"
								className={classes.headerImage}
								src={servedImage("/images/bn-logo.png")}
							/>
						</Link>
						<Hidden smDown>
							<div className={classes.verticalDivider}/>
						</Hidden>

						<Hidden smDown>
							<BoxOfficeEventSelection type="top-bar"/>
						</Hidden>
					</div>
					<span className={classes.rightMenuOptions}>
						<Hidden smDown>
							<BoxOfficeLink/>
						</Hidden>
						<RightUserMenu history={history}/>
					</span>
				</Toolbar>
			</AppBar>
			<Hidden mdUp>
				<BoxOfficeEventSelection type="stand-alone"/>
			</Hidden>
		</div>
	);
};

BoxOfficeAppBar.defaultProps = {
	homeLink: "/"
};

BoxOfficeAppBar.propTypes = {
	classes: PropTypes.object.isRequired,
	handleDrawerToggle: PropTypes.func,
	history: PropTypes.object.isRequired,
	homeLink: PropTypes.string
};

export default withStyles(styles)(BoxOfficeAppBar);
