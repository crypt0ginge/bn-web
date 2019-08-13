import React, { Component } from "react";
import { Typography, withStyles } from "@material-ui/core";
import QRCode from "qrcode.react";
import moment from "moment-timezone";

import notifications from "../../../../stores/notifications";
import Bigneon from "../../../../helpers/bigneon";
import Loader from "../../../elements/loaders/Loader";
import TransferContainer from "../transfers/TransferContainer";
import EventCardContainer from "../transfers/EventCardContainer";
import getUrlParam from "../../../../helpers/getUrlParam";
import { observer } from "mobx-react";
import layout from "../../../../stores/layout";

const styles = theme => ({
	root: {
		display: "flex",
		justifyContent: "center",
		flexDirection: "column",
		alignItems: "center"
	},
	ticketContainer: {
		marginBottom: "10%",
		width: "100%",
		maxWidth: 600
	},
	qrContainer: {
		padding: 10,
		display: "flex",
		justifyContent: "center"
	},
	redeemedImageContainer: {
		padding: 10,
		backgroundColor: "rgba(222, 226, 232, 0.4)",
		display: "flex",
		justifyContent: "center",
		flexDirection: "column",
		alignItems: "center"
	},
	redeemedImage: {
		//margin: 20,
		width: "100%",
		maxWidth: 250,
		height: "auto"
	},
	redeemedText: {
		color: "#8b8b8b",
		textAlign: "center",
		marginTop: 10,
		fontSize: 14
	}
});

class Tickets extends Component {
	constructor(props) {
		super(props);

		this.state = {
			tickets: null
		};

		this.refreshTickets = this.refreshTickets.bind(this);
	}

	componentDidMount() {
		const eventId = getUrlParam("event_id");

		this.loadTickets();

		if (eventId) {
			this.loadEventDetails(eventId);
		}

		this.refreshInterval = setInterval(this.refreshTickets, 1000);
	}

	componentWillUnmount() {
		if (this.refreshInterval) {
			clearInterval(this.refreshInterval);
		}
	}

	refreshTickets() {
		const { windowIsActive } = layout;

		if (windowIsActive && !this.isLoadingTickets) {
			this.loadTickets();
		}
	}

	loadEventDetails(id) {
		Bigneon()
			.events.read({ id })
			.then(response => {
				const { name, promo_image_url, venue, event_start } = response.data;

				this.setState({
					eventName: name,
					eventImageUrl: promo_image_url,
					eventAddress: `${venue.name}, ${venue.address}, ${venue.city}`,
					eventDisplayTime: moment
						.utc(event.event_start)
						.tz(venue.timezone)
						.format("ddd, MMM Do YYYY")
				});
			})
			.catch(error => {
				this.setState({ isCancelling: false });
				console.error(error);
				notifications.showFromErrorResponse({
					error,
					defaultMessage: "Failed to load event."
				});
			});
	}

	loadTickets() {
		this.isLoadingTickets = true;

		//TODO
		setTimeout(() => {
			this.setState({
				tickets: [
					{ id: "test" },
					{ id: "test2", status: "Redeemed" },
					{ id: "test3" }
				]
			});

			this.isLoadingTickets = false;
		}, 500);
	}

	render() {
		const {
			tickets,
			eventName,
			eventImageUrl,
			eventAddress,
			eventDisplayTime
		} = this.state;
		const { classes } = this.props;

		if (!tickets) {
			return <Loader>Loading tickets...</Loader>;
		}

		return (
			<TransferContainer>
				<div className={classes.root}>
					{tickets.map((ticket, index) => {
						const ticketType = "General access";
						const qrText = "TODO";
						const isRedeemable = ticket.status !== "Redeemed"; //TODO check other status when data comes in

						return (
							<div key={index} className={classes.ticketContainer}>
								<EventCardContainer
									name={`1 x ${ticketType} - ${eventName}`}
									imageUrl={eventImageUrl}
									address={eventAddress}
									displayDate={eventDisplayTime}
									imageStyle={{ height: 160 }}
								>
									{isRedeemable ? (
										<div className={classes.qrContainer}>
											<QRCode
												style={{
													width: "100%",
													height: "auto",
													maxWidth: 350
												}}
												size={350}
												value={qrText}
											/>
										</div>
									) : (
										<div className={classes.redeemedImageContainer}>
											<img
												className={classes.redeemedImage}
												src={"/images/bn-logo-white.png"}
											/>
											<Typography className={classes.redeemedText}>
												Ticket cannot be redeemed at this time.
											</Typography>
										</div>
									)}
								</EventCardContainer>
							</div>
						);
					})}
				</div>
			</TransferContainer>
		);
	}
}

export default withStyles(styles)(Tickets);
