import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";

import { Grid, Typography } from "@material-ui/core";
import Card from "../../../elements/Card";
import {
	fontFamilyBold,
	fontFamilyDemiBold,
	secondaryHex
} from "../../../../config/theme";

const styles = theme => ({
	root: {},
	headingText: {
		color: "#FFFFFF",
		fontFamily: fontFamilyBold,
		fontSize: 28,
		textAlign: "center"
	},
	eventNameText: {
		color: "#FFFFFF",
		fontFamily: fontFamilyBold,
		fontSize: 28
	},
	media: {
		height: 260,
		width: "100%",
		backgroundImage: "linear-gradient(255deg, #e53d96, #5491cc)",
		backgroundRepeat: "no-repeat",
		backgroundSize: "cover",
		backgroundPosition: "center"
	},
	mediaOverlay: {
		height: "100%",
		width: "100%",
		backgroundColor: "rgba(27, 33, 91, 0.3)",
		display: "flex",
		flexDirection: "column",

		padding: theme.spacing.unit * 4,
		paddingBottom: theme.spacing.unit * 2,

		[theme.breakpoints.down("sm")]: {
			padding: theme.spacing.unit * 2,
			paddingBottom: theme.spacing.unit
		}
	},
	topRow: {
		flex: 1
	},
	bottomRow: {
		flex: 1,
		display: "flex",
		alignItems: "flex-end"
	},
	detailsText: {
		fontSize: 14,
		color: secondaryHex,
		backgroundColor: "#FFFFFF",
		fontFamily: fontFamilyDemiBold,
		marginBottom: 10,
		paddingLeft: 12,
		paddingRight: 12,
		paddingTop: 6,
		paddingBottom: 2,
		display: "inline"
	}
});

const EventCardContainer = props => {
	const {
		classes,
		title,
		children,
		name,
		imageUrl,
		displayDate,
		address,
		imageStyle
	} = props;

	const mediaStyle = imageUrl
		? { backgroundImage: `url(${imageUrl})`, ...imageStyle }
		: imageStyle;

	return (
		<React.Fragment>
			<Grid justify={"center"} container>
				<Grid item xs={12} sm={8} lg={12}>
					<Typography className={classes.headingText}>{title}</Typography>
				</Grid>
				<Grid item xs={12} sm={8} lg={6}>
					<Card variant="raisedLight" className={classes.card}>
						<div>
							<div className={classes.media} style={mediaStyle}>
								<div className={classes.mediaOverlay}>
									<div className={classes.topRow}>
										{displayDate ? (
											<Typography className={classes.detailsText}>
												{displayDate}
											</Typography>
										) : null}
										<div style={{ marginTop: 8 }}/>
										{address ? (
											<Typography className={classes.detailsText}>
												{address}
											</Typography>
										) : null}
									</div>

									<div className={classes.bottomRow}>
										<Typography
											variant={"display1"}
											className={classes.eventNameText}
										>
											{name}
										</Typography>
									</div>
								</div>
							</div>

							{children}
						</div>
					</Card>
				</Grid>
			</Grid>
		</React.Fragment>
	);
};

EventCardContainer.defaultPropTypes = {
	name: "name",
	imageStyle: {}
};

EventCardContainer.propTypes = {
	title: PropTypes.string,
	classes: PropTypes.object.isRequired,
	children: PropTypes.oneOfType([PropTypes.element, PropTypes.array])
		.isRequired,
	name: PropTypes.string,
	imageUrl: PropTypes.string,
	displayDate: PropTypes.string,
	address: PropTypes.string,
	imageStyle: PropTypes.object
};

export default withStyles(styles)(EventCardContainer);
